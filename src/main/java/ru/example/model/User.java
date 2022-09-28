package ru.example.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.example.model.abstractentity.Person;
import ru.example.model.fieldenum.UserRole;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "user", schema = "public")
public class User extends Person implements Serializable, UserDetails {
    @Id
    @SequenceGenerator(name = "user_seq", sequenceName = "user_user_id_seq", allocationSize = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @Column(name = "user_id", nullable = false)
    @Min(value = 0L, message = "Значение id должно быть положительным")
    private Long id;

    @Column(name = "login", nullable = false, unique = true)
    @Size(min = 4, max = 30, message = "Логин должен быть не меньше 4 и не больше 30 символов")
    private String login;

    @Column(name = "password", nullable = false)
    @Size(min = 4, message = "Пароль должен быть не меньше 4 символов")
    private String password;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "email")
    @Size(max = 255, message = "Email должен быть не больше 255 символов")
    @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Email должен быть корректен")
    private String email;

    public User() {
    }

    public User(String name, String address, String phoneNumber, Long id, String login, String password, UserRole role, String email) {
        super(name, address, phoneNumber);
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
        this.email = email;
    }

    public User(String login, String password, UserRole roleUser) {
        this.login = login;
        this.password = password;
        this.role = roleUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>(List.of(new SimpleGrantedAuthority(role.toString())));
    }

    @Override
    public String getUsername() {
        return this.login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        if (o instanceof HibernateProxy) {
            user = (User) Hibernate.unproxy(o);
        }
        return Objects.equals(id, user.id)
                && Objects.equals(login, user.login)
                && Objects.equals(password, user.password)
                && role == user.role
                && name.equals(user.getName())
                && address.equals(user.getAddress())
                && phoneNumber.equals(user.getAddress())
                && email.equals(user.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, role, name, address, phoneNumber, email);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("id=").append(id);
        sb.append(", login='").append(login).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", role=").append(role);
        sb.append(", email='").append(email).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", phoneNumber='").append(phoneNumber).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
