package com.narvasoft.apirest.service;

import com.narvasoft.apirest.models.Usuarios;
import com.narvasoft.apirest.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuariosServiceImpl implements UsuariosService {

    @Autowired
    private UsuariosRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    @Transactional(readOnly = true)
    public Iterable<Usuarios> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Usuarios> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuarios> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public Usuarios save(Usuarios user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuarios> findByEmail(String email) {
        if (email == null) return Optional.empty();
        return userRepository.findByEmail(email.trim().toLowerCase());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuarios> login(String email, String password) {
        if (email == null || password == null) return Optional.empty();
        return userRepository.findByEmail(email.trim().toLowerCase())
                .filter(u -> u.getPassword() != null && encoder.matches(password, u.getPassword()));
    }
}

