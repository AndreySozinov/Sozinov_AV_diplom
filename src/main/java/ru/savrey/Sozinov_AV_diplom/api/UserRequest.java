package ru.savrey.Sozinov_AV_diplom.api;

import lombok.Data;

@Data
public class UserRequest {
    private String lastName;
    private String firstName;
    private String patronymic;
    private String phone;
    private String email;
    private Roles role;
    private String login;
    private String password;
}
