package ru.kata.spring.boot_security.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    int countByEmail(String email);
    int countByEmailAndIdNot(String email, Long id);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email = :email")
    Optional<User> findByEmail(String email);

    @Query("SELECT DISTINCT u FROM User u JOIN FETCH u.roles r " +
            "WHERE r.name = 'ROLE_USER' " +
            "AND NOT EXISTS (SELECT r2 FROM u.roles r2 WHERE r2.name = 'ROLE_ADMIN')")
    List<User> findAllUsersWithUserRole();
}
