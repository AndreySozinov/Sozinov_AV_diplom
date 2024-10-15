package ru.savrey.Sozinov_AV_diplom.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.savrey.Sozinov_AV_diplom.model.User;
import ru.savrey.Sozinov_AV_diplom.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Controller
@RequestMapping("/api/user/")
@Tag(name = "User")
public class UserController {

    private UserService userService;

    //region Добавление нового пользователя
    @PostMapping
    @Operation(summary = "create new user", description = "Добавляет нового пользователя")
    public String createUser(@RequestBody UserRequest request, Model model) {
        final User user;
        try {
            user = userService.createUser(request);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
        model.addAttribute("message", "Пользователь добавлен");
        model.addAttribute("user", user);
        return "user/user";
    }
    //endregion

    //region Редактирование пользователя
    @PostMapping(path = "/update/{id}")
    @Operation(summary = "update user", description = "Редактирует информацию пользователя")
    public String updateUser(@PathVariable long id, @RequestBody UserRequest request, Model model) {
        final User user;
        try {
            user = userService.updateUser(id, request);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
        model.addAttribute("message", "Пользователь обновлен");
        model.addAttribute("user", user);
        return "user/user";
    }
    //endregion

    //region Список пользователей в БД
    @GetMapping(path = "/all")
    @Operation(summary = "get all users list", description = "Загружает список всех пользователей из БД")
    public String getAllUsers(Model model) {
        final List<User> users;
        try {
            users = userService.getAllUsers();
        } catch (RuntimeException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
        model.addAttribute("message", "Получен список пользователей");
        model.addAttribute("users", users);
        return "user/users";
    }
    //endregion

    //region Загрузка пользователя по ID из БД
    @GetMapping(path = "/{id}")
    @Operation(summary = "get user by ID", description = "Загружает пользователя из базы данных по ID")
    public String getUserInfo(@PathVariable long id, Model model) {
        final User user;
        try {
            user = userService.getUserById(id);
        } catch (NoSuchElementException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
        model.addAttribute("message", "Пользователь найден");
        model.addAttribute("user", user);
        return "user/user";
    }
    //endregion

    //region Удаление пользователя из БД
    @GetMapping(path = "/delete/{id}")
    @Operation(summary = "delete user by ID", description = "Удаляет пользователя из базы данных по ID")
    public String deleteUser(@PathVariable long id, Model model) {
        final User user;
        try {
            user = userService.deleteUser(id);
        } catch (NoSuchElementException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
        model.addAttribute("message", "Пользователь удален");
        model.addAttribute("user", user);
        return "user/user";
    }
    //endregion
}
