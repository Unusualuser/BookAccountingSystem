package ru.example.dto;

import java.io.Serializable;

public class AuthResponseDto implements Serializable {
    private String login;
    private String accessToken;

    public AuthResponseDto() {
    }

    public AuthResponseDto(String login, String accessToken) {
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
