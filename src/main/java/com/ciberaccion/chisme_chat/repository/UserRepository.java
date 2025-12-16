package com.ciberaccion.chisme_chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ciberaccion.chisme_chat.model.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}