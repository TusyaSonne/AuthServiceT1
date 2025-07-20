package ru.dzhenbaz.AuthServiceT1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dzhenbaz.AuthServiceT1.model.RevokedToken;

import java.util.Optional;

public interface RevokedTokenRepository extends JpaRepository<RevokedToken, Long> {
    Optional<RevokedToken> findByToken(String token);
    boolean existsByToken(String token);
}
