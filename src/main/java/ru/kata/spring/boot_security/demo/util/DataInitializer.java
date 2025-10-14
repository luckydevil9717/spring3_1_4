package ru.kata.spring.boot_security.demo.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final RoleService roleService;

    public DataInitializer(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public void run(String... args) {
        List<Role> existingRoles = roleService.getAllRoles();
        if (existingRoles.isEmpty()) {
            roleService.saveRole(new Role("ROLE_USER"));
            roleService.saveRole(new Role("ROLE_ADMIN"));
        }

        Role roleUser = roleService.getAllRoles().stream()
                .filter(r -> r.getName().equals("ROLE_USER"))
                .findFirst()
                .orElseThrow();
        Role roleAdmin = roleService.getAllRoles().stream()
                .filter(r -> r.getName().equals("ROLE_ADMIN"))
                .findFirst()
                .orElseThrow();


        if (userService.findByEmail("admin@mail.ru").isEmpty()) {
            User admin = new User("Admin", "Admin", 30, "admin@mail.ru", "admin");
            admin.setRoles(new HashSet<>(Set.of(roleAdmin, roleUser)));
            userService.saveUser(admin);
        }


        if (userService.findByEmail("user@mail.ru").isEmpty()) {
            User user = new User("User", "User", 25, "user@mail.ru", "user");
            user.setRoles(new HashSet<>(Set.of(roleUser)));
            userService.saveUser(user);
        }
    }
}