package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.AdminDTO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.mapper.AdminMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminRestController {

    private final UserService userService;
    private final RoleService roleService;
    private final AdminMapper adminMapper;

    public AdminRestController(UserService userService, RoleService roleService, AdminMapper adminMapper) {
        this.userService = userService;
        this.roleService = roleService;
        this.adminMapper = adminMapper;
    }

    @GetMapping
    public List<AdminDTO> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(adminMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminDTO> getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        if (user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(adminMapper.toDto(user));
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody AdminDTO adminDTO) {
        User user = adminMapper.toEntity(adminDTO);
        userService.saveUserWithRoles(user, adminDTO.getRoleIds());
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @RequestBody AdminDTO adminDTO) {
        User user = adminMapper.toEntity(adminDTO);
        user.setId(id);
        userService.updateUserWithRoles(user, adminDTO.getRoleIds());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}