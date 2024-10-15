package ru.savrey.Sozinov_AV_diplom.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.savrey.Sozinov_AV_diplom.api.Roles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "users")
//@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@AllArgsConstructor
@RequiredArgsConstructor
@Schema(name = "Пользователь")
public class User implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(name = "ID")
    private Long id;

    @Column(name = "lastname", nullable = false)
    @Schema(name = "Фамилия", minLength = 3, maxLength = 20)
    @NotBlank(message = "Фамилия обязательна")
    private String lastname;

    @Column(name = "firstname")
    @Schema(name = "Имя", minLength = 3, maxLength = 20)
    private String firstname;

    @Column(name = "patronymic")
    @Schema(name = "Отчество", minLength = 3, maxLength = 20)
    private String patronymic;

    @Column(name = "phone")
    @Schema(name = "Телефон", minLength = 5, maxLength = 20)
    private String phone;

    @Column(name = "email")
    @Schema(name = "Email", minLength = 5, maxLength = 20)
    @Email(message = "Email некорректен")
    private String email;

//    @Column(name = "role")
//    @Schema(name = "Роль", nullable = false)
//    private Roles role;

    @Column(name = "login")
    @Schema(name = "Логин", minLength = 3, maxLength = 20)
    private String login;

    @Column(name = "password")
    @Schema(name = "Пароль", minLength = 6, maxLength = 20)
    private String password;

//    @OneToOne(mappedBy = "user",
//            fetch = FetchType.LAZY,
//            cascade = CascadeType.ALL)
//    private Farm farm = new Farm();

//    public void addFarm(Farm farm) {
//        this.farm = farm;
//    }
//
//    public void removeFarm() {
//        this.farm = null;
//    }

    public User(String lastname, String login, String password) {
        this.lastname = lastname;
        this.login = login;
        this.password = password;
    }

    public User(String lastname, String firstname, String patronymic,
                String phone, String email, String login, String encode) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.patronymic = patronymic;
        this.phone = phone;
        this.email = email;
        this.login = login;
        this.password = encode;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return login;
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
}
