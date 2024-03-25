package ru.savrey.Sozinov_AV_diplom.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.savrey.Sozinov_AV_diplom.api.Roles;

@Entity
@Table(name = "users")
@Data
@RequiredArgsConstructor
@Schema(name = "Пользователь")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(name = "ID")
    private long id;

    @Column(name = "lastname", nullable = false)
    @Schema(name = "Фамилия", minLength = 3, maxLength = 20)
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
    private String email;

    @Column(name = "role")
    @Schema(name = "Роль", nullable = false)
    private Roles role;

    @Column(name = "login")
    @Schema(name = "Логин", minLength = 3, maxLength = 20)
    private String login;

    @Column(name = "password")
    @Schema(name = "Пароль", minLength = 6, maxLength = 20)
    private String password;

    public User(String lastname, Roles role, String login, String password) {
        this.lastname = lastname;
        this.role = role;
        this.login = login;
        this.password = password;
    }
}
