package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public void saveUser(User user) {
        encodePasswordIfPresent(user);
        userRepo.save(user);
    }

    @Override
    public User getUser(Long id) {
        return userRepo.findById(id).orElse(null);
    }

    @Override
    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    @Override
    public void saveUserWithRoles(User user, Long[] roleIds) {
        Set<Role> roles = getRolesByIds(roleIds);
        user.setRoles(roles);
        encodePasswordIfPresent(user);
        userRepo.save(user);
    }

    @Override
    public void updateUserWithRoles(User user, Long[] roleIds) {
        User existingUser = userRepo.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setAge(user.getAge());
        existingUser.setEmail(user.getEmail());


        if (user.getPassword() != null && !user.getPassword().isEmpty() && !user.getPassword().startsWith("$2a$")) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        existingUser.setRoles(getRolesByIds(roleIds));
        userRepo.save(existingUser);
    }
    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepo.findAll();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepo.countByEmail(email) > 0;
    }

    @Override
    public boolean existsByEmailExceptId(String email, Long id) {
        return userRepo.countByEmailAndIdNot(email, id) > 0;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList())
        );
    }
    @Override
    public List<User> findAllWithUserRole() {
        return userRepo.findAllUsersWithUserRole();
    }


    private void encodePasswordIfPresent(User user) {
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            String encoded = passwordEncoder.encode(user.getPassword());
            System.out.println("Encoding password for user " + user.getEmail() + ": " + encoded);
            user.setPassword(encoded);
        }
    }

    private Set<Role> getRolesByIds(Long[] roleIds) {
        return Arrays.stream(roleIds)
                .map(roleRepo::findById)
                .map(opt -> opt.orElseThrow(() -> new RuntimeException("Role not found")))
                .collect(Collectors.toSet());
    }
}