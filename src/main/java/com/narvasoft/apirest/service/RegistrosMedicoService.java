package com.narvasoft.apirest.service;

import com.narvasoft.apirest.models.RegistrosMedico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RegistrosMedicoService {
    public Iterable<RegistrosMedico> findAll();
    public Page<RegistrosMedico> findAll(Pageable pageable);
    public Optional<RegistrosMedico> findById(Long id);
    public RegistrosMedico save(RegistrosMedico registro);
    public void deleteById(Long id);
}