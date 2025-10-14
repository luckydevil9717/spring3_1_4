package ru.kata.spring.boot_security.demo.mapper;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dto.AdminDTO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AdminMapper {

    private final RoleService roleService;

    public AdminMapper(RoleService roleService) {
        this.roleService = roleService;
    }


    public AdminDTO toDto(User user) {
        if (user == null) return null;
        AdminDTO dto = new AdminDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setAge(user.getAge());
        dto.setEmail(user.getEmail());

        if (user.getRoles() != null) {
            dto.setRolesNames(user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet()));
            dto.setRoleIds(user.getRoles().stream()
                    .map(Role::getId)
                    .toArray(Long[]::new));
        }
        return dto;
    }

    public User toEntity(AdminDTO dto) {
        if (dto == null) return null;
        User user = new User();
        user.setId(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setAge(dto.getAge());
        user.setEmail(dto.getEmail());


        if (dto.getRoleIds() != null) {
            Set<Role> roles = Arrays.stream(dto.getRoleIds())
                    .map(roleService::getRoleById)
                    .filter(r -> r != null)
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }
        return user;
    }
}