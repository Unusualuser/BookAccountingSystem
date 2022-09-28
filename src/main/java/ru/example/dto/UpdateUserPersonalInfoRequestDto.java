package ru.example.dto;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class UpdateUserPersonalInfoRequestDto implements Serializable {
    @Size(max = 255, message = "Email должен быть не больше 255 символов")
    @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Email должен быть корректен")
    private String email;
    @Size(min = 4, max = 100, message = "Имя должно быть не меньше 4 и не больше 100 символов")
    private String name;
    @Size(max = 70, message = "Адрес должен быть не больше 70 символов")
    private String address;
    @Pattern(regexp = "\\+7[0-9]{10}", message = "Номер телефона должен быть корректен. Пример: +79999999999")
    private String phoneNumber;

    public UpdateUserPersonalInfoRequestDto() {
    }

    public UpdateUserPersonalInfoRequestDto(String email, String name, String address, String phoneNumber) {
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
