package com.sufaka.libraryspringboot.controller;

import com.sufaka.libraryspringboot.model.User;
import com.sufaka.libraryspringboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Yeni kullanıcı kaydı
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Bu e-posta zaten kullanılıyor!");
        }

        userRepository.save(user);
        return ResponseEntity.ok("Kullanıcı başarıyla kaydedildi!");
    }
}
