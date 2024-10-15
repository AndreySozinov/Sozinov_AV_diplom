package ru.savrey.Sozinov_AV_diplom.api;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.savrey.Sozinov_AV_diplom.model.Farm;
import ru.savrey.Sozinov_AV_diplom.model.Field;
import ru.savrey.Sozinov_AV_diplom.model.Point;
import ru.savrey.Sozinov_AV_diplom.model.User;
import ru.savrey.Sozinov_AV_diplom.service.*;

import java.util.NoSuchElementException;

@Controller
public class UIController {

    //region Подключенные сервисы
    @Autowired
    private FarmService farmService;

    @Autowired
    private FieldService fieldService;

    @Autowired
    private PointService pointService;

    @Autowired
    private UserService userService;
    //endregion

    @GetMapping("/")
    @Operation(summary = "main page", description = "Главная страница")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    @Operation(summary = "login page", description = "Страница входа")
    public String login() { return "login"; }

    //region Создание нового объекта
    @GetMapping("/farm_form")
    @Operation(summary = "farm form", description = "Заполнение информации по хозяйству")
    public String farmForm() {
        return "farm/farm_form";
    }

    @GetMapping("/field_form/{farmId}")
    @Operation(summary = "field form", description = "Заполнение информации по полю")
    public String fieldForm(@PathVariable long farmId, Model model) {
        final Farm farm;
        try {
            farm = farmService.getFarmById(farmId);
        } catch (NoSuchElementException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
        model.addAttribute("message", "Новое поле");
        model.addAttribute("farmId", farmId);
        return "field/field_form";
    }

    @GetMapping("/point_form/{fieldId}")
    @Operation(summary = "point form", description = "Заполнение информации по точке")
    public String pointForm(@PathVariable long fieldId, Model model) {
        final Field field;
        try {
            field = fieldService.getFieldById(fieldId);
        } catch (NoSuchElementException ex) {
            model.addAttribute("message",  ex.getMessage());
            return "error";
        }
        model.addAttribute("message",  "Новая точка");
        model.addAttribute("fieldId", fieldId);
        return "point/point_form";
    }

    @GetMapping("/user_form")
    @Operation(summary = "user form", description = "Заполнение информации по пользователю")
    public String userForm() {
        return "user_form";
    }
    //endregion

    //region Редактирование существующего объекта
    @GetMapping("/farm_update/{farmId}")
    @Operation(summary = "farm update", description = "Редактирование информации по хозяйству")
    public String farmUpdate(@PathVariable long farmId, Model model) {
        final Farm farm;
        try {
            farm = farmService.getFarmById(farmId);
        } catch (NoSuchElementException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
        model.addAttribute("message", "Редактирование данных о хозяйстве");
        model.addAttribute("farm", farm);
        return "farm/farm_update";
    }

    @GetMapping("/field_update/{fieldId}")
    @Operation(summary = "field update", description = "Редактирование информации по полю")
    public String fieldUpdate(@PathVariable long fieldId, Model model) {
        final Field field;
        try {
            field = fieldService.getFieldById(fieldId);
        } catch (NoSuchElementException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
        model.addAttribute("message", "Редактирование данных о поле");
        model.addAttribute("field", field);
        return "field/field_update";
    }

    @GetMapping("/point_update/{pointId}")
    @Operation(summary = "point update", description = "Редактирование информации по точке")
    public String pointUpdate(@PathVariable long pointId, Model  model) {
        final Point point;
        try {
            point = pointService.getPointById(pointId);
        } catch (NoSuchElementException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
        model.addAttribute("message", "Редактирование данных о точке");
        model.addAttribute("point", point);
        return "point/point_update";
    }

    @GetMapping("/user_update/{userId}")
    @Operation(summary = "user update", description = "Редактирование информации о пользователе")
    public String userUpdate(@PathVariable long userId, Model model) {
        final User user;
        try {
            user = userService.getUserById(userId);
        } catch (NoSuchElementException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
        model.addAttribute("message", "Редактирование информации о пользователе");
        model.addAttribute("user", user);
        return "user/user_update";
    }
    //endregion

    //region Удаление объекта
    @GetMapping("/farm_delete/{farmId}")
    @Operation(summary = "farm delete", description = "Удаление информации о хозяйстве")
    public String farmDelete(@PathVariable long farmId, Model model) {
        model.addAttribute("farmId", farmId);
        return "farm/farm_delete";
    }

    @GetMapping("/field_delete/{fieldId}")
    @Operation(summary = "field delete", description = "Удаление информации о поле")
    public String fieldDelete(@PathVariable long fieldId, Model model) {
        model.addAttribute("fieldId", fieldId);
        return "field/field_delete";
    }

    @GetMapping("/point_delete/{pointId}")
    @Operation(summary = "point delete", description = "Удаление информации о точке")
    public String pointDelete(@PathVariable long pointId, Model model) {
        model.addAttribute("pointId", pointId);
        return "point/point_delete";
    }

    @GetMapping("/user_delete/{userId}")
    @Operation(summary = "user delete", description = "Удаление информации о пользователе")
    public String userDelete(@PathVariable long userId, Model model) {
        model.addAttribute("userId", userId);
        return "user/user_delete";
    }
    //endregion

    @GetMapping("/rate/{fieldId}")
    @Operation(summary = "rate NPK computing", description = "Расчеты нормы NPK")
    public String rateCompute(@PathVariable long fieldId, Model model) {
        final Field field;
        try {
            field = fieldService.getFieldById(fieldId);
        } catch (NoSuchElementException ex) {
            model.addAttribute("message",  ex.getMessage());
            return "error";
        }
        model.addAttribute("message",  "Дозы NPK");
        model.addAttribute("field", field);
        return "rate";
    }
}
