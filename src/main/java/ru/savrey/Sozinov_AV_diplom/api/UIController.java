package ru.savrey.Sozinov_AV_diplom.api;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UIController {
    @GetMapping("/")
    @Operation(summary = "main page", description = "Главная страница")
    public String index() {
        return "index";
    }

    @GetMapping("/farm_form")
    @Operation(summary = "farm form", description = "Заполнение информации по хозяйству")
    public String farmForm() {
        return "farm_form";
    }

    @GetMapping("/field_form")
    @Operation(summary = "field form", description = "Заполнение информации по полю")
    public String fieldForm() {
        return "field_form";
    }

    @GetMapping("/point_form")
    @Operation(summary = "point form", description = "Заполнение информации по точке")
    public String pointForm() {
        return "point_form";
    }

    @GetMapping("/user_form")
    @Operation(summary = "user form", description = "Заполнение информации по пользователю")
    public String userForm() {
        return "user_form";
    }
}
