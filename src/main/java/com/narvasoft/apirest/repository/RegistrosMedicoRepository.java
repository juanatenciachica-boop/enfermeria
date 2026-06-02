package com.narvasoft.apirest.repository;

import com.narvasoft.apirest.models.RegistrosMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrosMedicoRepository extends JpaRepository<RegistrosMedico, Long> {
}