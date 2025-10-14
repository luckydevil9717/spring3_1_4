package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.mapper.UserMapper;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;



import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserRestController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserRestController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/user/me")
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal) {
        if (principal == null || principal.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<User> userOptional = userService.findByEmail(principal.getName());
        return userOptional
                .map(userMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.findAll().stream()
                .filter(u -> u.getRoles().stream().anyMatch(r -> "ROLE_USER".equals(r.getName())))
                .filter(u -> u.getRoles().stream().noneMatch(r -> "ROLE_ADMIN".equals(r.getName())))
                .map(userMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }
}