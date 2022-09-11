package ru.senla.model.abstractentity;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public abstract class Person implements Serializable {
    @Column(name = "name")
    @Size(min = 4, max = 100, message = "Имя должно быть не меньше 4 и не больше 100 символов")
    protected String name;

    @Column(name = "address")
    @Size(max = 70, message = "Адрес должен быть не больше 70 символов")
    protected String address;

    @Column(name = "phone_number")
    @Pattern(regexp = "\\+7[0-9]{10}", message = "Номер телефона должен быть корректен. Пример: +79999999999")
    protected String phoneNumber;

    public Person() {

    }

    public Person(String name, String address, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setName(String name) {
        this.name = name;
    }
}
