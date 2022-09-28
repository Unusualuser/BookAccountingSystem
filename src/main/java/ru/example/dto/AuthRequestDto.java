package ru.example.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class AuthRequestDto implements Serializable {
    @NotNull
    @Size(min = 4, max = 30, message = "Логин должен быть не меньше 4 и не больше 30 символов")
    private String login;
    @NotNull
    @Size(min = 4, message = "Пароль должен быть не меньше 4 символов")
    private String password;

    public AuthRequestDto() {
    }

    public AuthRequestDto(String login, String password) {
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
