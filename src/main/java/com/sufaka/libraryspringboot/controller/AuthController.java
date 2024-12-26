package com.sufaka.libraryspringboot.controller;

import com.sufaka.libraryspringboot.model.User;
import com.sufaka.libraryspringboot.repository.UserRepository;
import com.sufaka.libraryspringboot.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser == null || !existingUser.getPassword().equals(user.getPassword())) {
            return ResponseEntity.status(401).body("Geçersiz email veya şifre");
        }

        String token = JwtUtil.generateToken(existingUser.getEmail(), existingUser.getRole());
        return ResponseEntity.ok(token);
    }
}