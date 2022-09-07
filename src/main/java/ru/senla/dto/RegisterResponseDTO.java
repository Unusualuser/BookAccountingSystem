package ru.senla.dto;

import ru.senla.model.fieldenum.UserRole;

import java.io.Serializable;

public class RegisterResponseDTO implements Serializable {
    private Long id;
    private String login;
    private UserRole role;

    public RegisterResponseDTO() {
    }

    public RegisterResponseDTO(Long id, String login, UserRole role) {
        this.id = id;
        this.login = login;
        this.role = role;
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

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
