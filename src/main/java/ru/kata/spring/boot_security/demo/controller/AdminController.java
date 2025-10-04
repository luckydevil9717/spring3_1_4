package ru.kata.spring.boot_security.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String adminPage(Model model, @AuthenticationPrincipal User currentUser, HttpServletRequest request) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("allRoles", roleService.getAllRoles());
        model.addAttribute("newUser", new User());
        model.addAttribute("userToEdit", new User());
        model.addAttribute("currentPath", request.getRequestURI());
        return "admin";
    }

    @GetMapping("/user")
    public String userPage(Model model, HttpServletRequest request, @AuthenticationPrincipal User currentUser) {
        model.addAttribute("currentPath", request.getRequestURI());
        model.addAttribute("currentUser", currentUser);
        return "user";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("newUser") User user, @RequestParam("roleIds") Long[] roleIds, Model model, @AuthenticationPrincipal User currentUser) {

        if (userService.existsByEmail(user.getEmail())) {
            model.addAttribute("errorMessage", "Пользователь с таким email уже существует!");
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("allRoles", roleService.getAllRoles());
            model.addAttribute("newUser", new User());
            return "admin";
        }

        userService.saveUserWithRoles(user, roleIds);
        return "redirect:/admin";
    }


    @PostMapping("/update")
    public String updateUser(@ModelAttribute("userToEdit") User user, @RequestParam("roleIds") Long[] roleIds, Model model, @AuthenticationPrincipal User currentUser) {

        if (userService.existsByEmailExceptId(user.getEmail(), user.getId())) {
            model.addAttribute("errorMessage", "Пользователь с таким email уже существует!");
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("allRoles", roleService.getAllRoles());
            model.addAttribute("newUser", new User());
            return "admin";
        }

        userService.updateUserWithRoles(user, roleIds);
        return "redirect:/admin";
    }


    @PostMapping("/delete")
    public String deleteUser(@RequestParam Long id, Model model, @AuthenticationPrincipal User currentUser) {

        userService.deleteUser(id);

        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("allRoles", roleService.getAllRoles());
        model.addAttribute("newUser", new User());
        return "redirect:/admin";
    }
}