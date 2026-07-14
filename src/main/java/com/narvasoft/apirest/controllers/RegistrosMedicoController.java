package com.narvasoft.apirest.controllers;

import com.narvasoft.apirest.models.RegistrosMedico;
import com.narvasoft.apirest.service.RegistrosMedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/registrosmedicos")
public class RegistrosMedicoController {

    @Autowired
    private RegistrosMedicoService registroService; // Principio de Inversión de Dependencias (IoD)

    // Create a new medical record
    @PostMapping
    public ResponseEntity<?> createRegistro(@RequestBody RegistrosMedico registro) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registroService.save(registro));
    }

    // Get a medical record by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> readOne(@PathVariable(value = "id") Long id) {
        Optional<RegistrosMedico> oRegistro = registroService.findById(id);
        if (!oRegistro.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(oRegistro.get());
    }

    // Update a medical record
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody RegistrosMedico registroDetails, @PathVariable(value = "id") Long id) {
        Optional<RegistrosMedico> oRegistro = registroService.findById(id);

        if (!oRegistro.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        RegistrosMedico registro = oRegistro.get();

        // Actualizamos todos los campos
        registro.setNombre_paciente(registroDetails.getNombre_paciente());
        registro.setApellido_paciente(registroDetails.getApellido_paciente());
        registro.setFecha_nacimiento(registroDetails.getFecha_nacimiento());
        registro.setGenero(registroDetails.getGenero());
        registro.setDiagnostico(registroDetails.getDiagnostico());
        registro.setTratamiento(registroDetails.getTratamiento());
        registro.setFecha_consulta(registroDetails.getFecha_consulta());
        registro.setNombre_medico(registroDetails.getNombre_medico());
        registro.setEspecialidad(registroDetails.getEspecialidad());
        registro.setObservaciones(registroDetails.getObservaciones());

        return ResponseEntity.status(HttpStatus.OK).body(registroService.save(registro));
    }

    // Delete a medical record
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
        if (!registroService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        registroService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // Get all medical records
    @GetMapping
    public List<RegistrosMedico> readAll() {
        List<RegistrosMedico> registros = StreamSupport
                .stream(registroService.findAll().spliterator(), false)
                .collect(Collectors.toList());
        return registros;
    }
}