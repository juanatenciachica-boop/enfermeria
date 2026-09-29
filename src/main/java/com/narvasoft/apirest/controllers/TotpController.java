package com.narvasoft.apirest.controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.narvasoft.apirest.models.UserTotpSecret;
import com.narvasoft.apirest.models.Usuarios;
import com.narvasoft.apirest.repository.UserTotpSecretRepository;
import com.narvasoft.apirest.service.UsuariosService;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/totp")
@CrossOrigin(origins = "*")
public class TotpController {

    private static final String ISSUER = "Salud INEM";
    private static final int MAX_INTENTOS = 5;
    private static final long BLOQUEO_MS = 5 * 60 * 1000L;
    private static final Pattern PASSWORD =
            Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&#\\-])[A-Za-z\\d@$!%*?&#\\-]{8,}$");

    private final GoogleAuthenticator gAuth = new GoogleAuthenticator();

    // email -> [fallos, bloqueadoHasta(ms)]  (en memoria; se reinicia al apagar el servidor)
    private final Map<String, long[]> intentos = new ConcurrentHashMap<>();

    @Autowired
    private UserTotpSecretRepository secretRepo;

    @Autowired
    private UsuariosService usuariosService;

    @Autowired
    private PasswordEncoder encoder;

    /* ===== 1) CONFIGURAR: correo + contraseña actual -> secreto y QR ===== */
    @PostMapping("/generar")
    public ResponseEntity<?> generarSecreto(@RequestBody Map<String, String> body) {
        String email = norm(body.get("email"));
        String password = body.get("password");

        if (email == null || password == null || password.isBlank()) {
            return bad("Correo y contraseña son obligatorios");
        }
        if (estaBloqueado(email)) return bloqueado();

        if (usuariosService.login(email, password).isEmpty()) {
            registrarFallo(email);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensaje", "Credenciales inválidas"));
        }

        Optional<UserTotpSecret> existente = secretRepo.findByEmail(email);
        if (existente.isPresent() && Boolean.TRUE.equals(existente.get().getEnabled())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("mensaje", "Google Authenticator ya está configurado para esta cuenta"));
        }

        String secretKey = gAuth.createCredentials().getKey();

        UserTotpSecret registro = existente.orElse(new UserTotpSecret());
        registro.setEmail(email);
        registro.setSecretKey(secretKey);
        registro.setCreatedAt(LocalDateTime.now());
        registro.setEnabled(false);
        secretRepo.save(registro);

        String otpauthUrl = "otpauth://totp/" + enc(ISSUER + ":" + email)
                + "?secret=" + secretKey
                + "&issuer=" + enc(ISSUER);

        String qrImagen = qrComoImagen(otpauthUrl);
        if (qrImagen == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("mensaje", "No se pudo generar el código QR"));
        }

        return ResponseEntity.ok(Map.of(
                "secret", secretKey,
                "qrUrl", otpauthUrl,
                "qrImage", qrImagen));
    }

    /* ===== 2) ACTIVAR: verifica el primer código de la app ===== */
    @PostMapping("/verificar")
    public ResponseEntity<?> verificarCodigo(@RequestBody Map<String, String> body) {
        String email = norm(body.get("email"));
        Integer codigo = parseCodigo(body.get("codigo"));

        if (email == null || codigo == null) return bad("Datos inválidos");
        if (estaBloqueado(email)) return bloqueado();

        Optional<UserTotpSecret> opt = secretRepo.findByEmail(email);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "No hay secreto configurado para este usuario"));
        }

        if (!gAuth.authorize(opt.get().getSecretKey(), codigo)) {
            registrarFallo(email);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensaje", "Código incorrecto o expirado"));
        }

        intentos.remove(email);
        opt.get().setEnabled(true);
        secretRepo.save(opt.get());

        return ResponseEntity.ok(Map.of("mensaje", "Google Authenticator activado correctamente"));
    }

    /* ===== 3) RECUPERAR CONTRASEÑA: correo + código + contraseña nueva ===== */
    @PostMapping("/recuperar")
    public ResponseEntity<?> recuperarPassword(@RequestBody Map<String, String> body) {
        String email = norm(body.get("email"));
        String nueva = body.get("nuevaPassword");
        Integer codigo = parseCodigo(body.get("codigo"));

        if (email == null || nueva == null || codigo == null) return bad("Datos incompletos");
        if (!PASSWORD.matcher(nueva).matches()) {
            return bad("La contraseña debe tener 8+ caracteres, letra, número y símbolo");
        }
        if (estaBloqueado(email)) return bloqueado();

        Optional<UserTotpSecret> opt = secretRepo.findByEmail(email);
        Optional<Usuarios> user = usuariosService.findByEmail(email);

        // Mismo mensaje para cualquier fallo: no se revela si el correo existe
        if (opt.isEmpty() || user.isEmpty()
                || !Boolean.TRUE.equals(opt.get().getEnabled())
                || !gAuth.authorize(opt.get().getSecretKey(), codigo)) {
            registrarFallo(email);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensaje", "Correo o código incorrecto"));
        }

        intentos.remove(email);
        user.get().setPassword(encoder.encode(nueva));
        usuariosService.save(user.get());

        return ResponseEntity.ok(Map.of("mensaje", "Contraseña actualizada correctamente"));
    }

    /* ===== Utilidades ===== */
    private String norm(String s) {
        return (s == null || s.isBlank()) ? null : s.trim().toLowerCase();
    }

    private Integer parseCodigo(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private String enc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8).replace("+", "%20");
    }

    // QR como imagen PNG en base64 (data URI): el secreto nunca sale del servidor hacia terceros
    private String qrComoImagen(String texto) {
        try {
            BitMatrix matriz = new QRCodeWriter().encode(texto, BarcodeFormat.QR_CODE, 240, 240);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matriz, "PNG", out);
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(out.toByteArray());
        } catch (WriterException | IOException e) {
            return null;
        }
    }

    private ResponseEntity<?> bad(String mensaje) {
        return ResponseEntity.badRequest().body(Map.of("mensaje", mensaje));
    }

    private ResponseEntity<?> bloqueado() {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(Map.of("mensaje", "Demasiados intentos. Intenta de nuevo en unos minutos."));
    }

    private boolean estaBloqueado(String email) {
        long[] i = intentos.get(email);
        if (i == null) return false;
        if (i[1] > System.currentTimeMillis()) return true;
        if (i[1] != 0) intentos.remove(email); // el bloqueo ya venció
        return false;
    }

    private void registrarFallo(String email) {
        long[] i = intentos.computeIfAbsent(email, k -> new long[2]);
        i[0]++;
        if (i[0] >= MAX_INTENTOS) {
            i[1] = System.currentTimeMillis() + BLOQUEO_MS;
            i[0] = 0;
        }
    }
}