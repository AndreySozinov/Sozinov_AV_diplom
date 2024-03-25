package ru.savrey.Sozinov_AV_diplom.service;

import ru.savrey.Sozinov_AV_diplom.api.UserRequest;
import ru.savrey.Sozinov_AV_diplom.model.User;

import java.util.List;

public interface UserService {

    User createUser(UserRequest request);
    User updateUser(Long id, UserRequest request);
    List<User> getAllUsers();
    User getUserById(Long id);
    User deleteUser(Long id);
}
