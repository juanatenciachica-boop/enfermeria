package com.narvasoft.apirest.service;

import com.narvasoft.apirest.models.RegistrosMedico;
import com.narvasoft.apirest.repository.RegistrosMedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class RegistrosMedicoServiceImpl implements RegistrosMedicoService {

    @Autowired
    private RegistrosMedicoRepository registroRepository; // Inyección de dependencias

    @Transactional(readOnly = true)
    public Iterable<RegistrosMedico> findAll() {
        return registroRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RegistrosMedico> findAll(Pageable pageable) {
        return registroRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RegistrosMedico> findById(Long id) {
        return registroRepository.findById(id);
    }

    @Override
    @Transactional
    public RegistrosMedico save(RegistrosMedico registro) {
        return registroRepository.save(registro);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        registroRepository.deleteById(id);
    }
}