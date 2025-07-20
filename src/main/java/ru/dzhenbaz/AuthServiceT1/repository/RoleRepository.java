package ru.dzhenbaz.AuthServiceT1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dzhenbaz.AuthServiceT1.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
    boolean existsByName(String name);
}
