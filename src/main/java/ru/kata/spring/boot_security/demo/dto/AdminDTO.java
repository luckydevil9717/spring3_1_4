package ru.kata.spring.boot_security.demo.dto;

import java.util.Set;

public class AdminDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private String email;
    private Long[] roleIds;
    private Set<String> rolesNames;

    public AdminDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Long[] roleIds) {
        this.roleIds = roleIds;
    }

    public Set<String> getRolesNames() {
        return rolesNames;
    }

    public void setRolesNames(Set<String> rolesNames) {
        this.rolesNames = rolesNames;
    }
}