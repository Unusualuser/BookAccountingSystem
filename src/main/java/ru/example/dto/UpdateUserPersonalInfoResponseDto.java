package ru.example.dto;

import java.io.Serializable;

public class UpdateUserPersonalInfoResponseDto implements Serializable {
    private String login;
    private String email;
    private String name;
    private String address;
    private String phoneNumber;

    public UpdateUserPersonalInfoResponseDto() {
    }

    public UpdateUserPersonalInfoResponseDto(String login, String email, String name, String address, String phoneNumber) {
        this.login = login;
        this.email = email;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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
