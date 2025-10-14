package ru.kata.spring.boot_security.demo.mapper;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.stream.Collectors;

@Component
public class UserMapper {


    public UserDTO toDto(User user) {
        if (user == null) return null;
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setAge(user.getAge());
        dto.setEmail(user.getEmail());
        dto.setPassword(null);
        if (user.getRoles() != null) {
            dto.setRolesNames(user.getRoles().stream()
                    .map(r -> r.getName())
                    .collect(Collectors.toSet()));
        }
        return dto;
    }


    public User toEntity(UserDTO dto) {
        if (dto == null) return null;
        User user = new User();
        user.setId(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setAge(dto.getAge());
        user.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(dto.getPassword());
        }
        return user;
    }
}