package com.narvasoft.apirest.repository;

import com.narvasoft.apirest.models.UserTotpSecret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserTotpSecretRepository extends JpaRepository<UserTotpSecret, Long> {
    Optional<UserTotpSecret> findByEmail(String email);
}