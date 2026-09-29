package com.narvasoft.apirest.service;

import com.narvasoft.apirest.models.Usuarios;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UsuariosService {

    Iterable<Usuarios> findAll();
    Page<Usuarios> findAll(Pageable pageable);
    Optional<Usuarios> findById(Long id);
    Usuarios save(Usuarios user);
    void deleteById(Long id);

    Optional<Usuarios> findByEmail(String email);      // ← NUEVO
    Optional<Usuarios> login(String email, String password); // ← NUEVO
}