package ru.senla.dto;

import java.io.Serializable;

public class AuthResponseDTO implements Serializable {
    private String login;
    private String accessToken;

    public AuthResponseDTO() {
    }

    public AuthResponseDTO(String login, String accessToken) {
        this.login = login;
        this.accessToken = accessToken;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
