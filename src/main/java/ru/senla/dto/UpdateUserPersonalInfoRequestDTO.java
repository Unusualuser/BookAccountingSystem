package ru.senla.dto;

import java.io.Serializable;

public class UpdateUserPersonalInfoRequestDTO implements Serializable {
    private String email;
    private String name;
    private String address;
    private String phoneNumber;

    public UpdateUserPersonalInfoRequestDTO() {
    }

    public UpdateUserPersonalInfoRequestDTO(String email, String name, String address, String phoneNumber) {
        this.email = email;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
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
