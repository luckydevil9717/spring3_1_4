package ru.kata.spring.boot_security.demo.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (roleService.getAllRoles().stream().noneMatch(r -> r.getName().equals("ROLE_USER"))) {
            roleService.saveRole(new Role("ROLE_USER"));
        }
        if (roleService.getAllRoles().stream().noneMatch(r -> r.getName().equals("ROLE_ADMIN"))) {
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


        if (userService.findByEmail("user@mail.ru").isEmpty()) {
            User user = new User("User", "User", 25, "user@mail.ru", "user");
            userService.saveUserWithRoles(user, new Long[]{roleUser.getId()});
        }


        if (userService.findByEmail("admin@mail.ru").isEmpty()) {
            User admin = new User("Admin", "Admin", 30, "admin@mail.ru", "admin");
            userService.saveUserWithRoles(admin, new Long[]{roleUser.getId(), roleAdmin.getId()});
        }
    }
}