package ru.senla.dto;

import java.io.Serializable;

public class AuthRequestDTO implements Serializable {
    private String login;
    private String password;

    public AuthRequestDTO() {
    }

    public AuthRequestDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
