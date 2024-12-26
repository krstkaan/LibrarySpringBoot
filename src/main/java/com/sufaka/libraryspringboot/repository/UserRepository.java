package com.sufaka.libraryspringboot.repository;

import com.sufaka.libraryspringboot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
