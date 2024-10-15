package ru.savrey.Sozinov_AV_diplom.security;

import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.savrey.Sozinov_AV_diplom.model.User;

@Data
public class RegistrationForm {

    private String lastname;
    private String firstname;
    private String patronymic;
    private String phone;
    private String email;
    private String login;
    private String password;

    public User toUser(PasswordEncoder passwordEncoder) {
        return new User(
                lastname, firstname, patronymic, phone, email,
                login, passwordEncoder.encode(password)
        );
    }
}
