package ru.dzhenbaz.AuthServiceT1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dzhenbaz.AuthServiceT1.model.Role;
import ru.dzhenbaz.AuthServiceT1.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
