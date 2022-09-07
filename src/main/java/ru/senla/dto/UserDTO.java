package ru.senla.dto;

import ru.senla.model.fieldenum.UserRole;

import java.io.Serializable;

public class UserDTO implements Serializable {
    private Long id;
    private String login;
    private UserRole userRole;
    private String email;
    private String name;
    private String address;
    private String phoneNumber;

    public UserDTO() {
    }

    public UserDTO(Long id, String login, UserRole userRole, String email, String name, String address, String phoneNumber) {
        this.id = id;
        this.login = login;
        this.userRole = userRole;
        this.email = email;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
