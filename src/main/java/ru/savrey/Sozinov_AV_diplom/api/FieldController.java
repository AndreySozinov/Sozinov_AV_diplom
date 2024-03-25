package ru.savrey.Sozinov_AV_diplom.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.savrey.Sozinov_AV_diplom.model.Farm;
import ru.savrey.Sozinov_AV_diplom.model.Field;
import ru.savrey.Sozinov_AV_diplom.service.FieldService;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Controller
@RequestMapping("/api/field/")
@Tag(name = "Field")
public class FieldController {

    @Autowired
    private FieldService fieldService;

    @PostMapping
    @Operation(summary = "create new field", description = "Добавляет новое поле")
    public String createField(@RequestBody FieldRequest request, Model model) {
        final Field field;
        try {
            field = fieldService.createField(request);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
        model.addAttribute("message", "Добавлено новое поле");
        model.addAttribute("field", field);
        return "field";
    }

    @PutMapping(path = "/{id}")
    @Operation(summary = "update field", description = "Редактирует поле")
    public String updateField(@PathVariable long id, @RequestBody FieldRequest request, Model model) {
        final Field field;
        try {
            field = fieldService.updateField(id, request);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
        model.addAttribute("message", "Поле изменено");
        model.addAttribute("field", field);
        return "field";
    }

    @GetMapping(path = "/all/{farm}")
    @Operation(summary = "get all fields list at farm", description = "Загружает список всех полей в хозяйстве из БД")
    public String getAllFields(@PathVariable Farm farm, Model model) {

        final List<Field> fields;
        try {
            fields = fieldService.getAllFieldsOnFarm(farm);
        } catch (RuntimeException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
        model.addAttribute("message", "Получен список полей");
        model.addAttribute("fields", fields);
        return "fields";
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "get field by ID", description = "Загружает поле из базы данных по ID")
    public String getFieldInfo(@PathVariable long id, Model model) {

        final Field field;
        try {
            field = fieldService.getFieldById(id);
        } catch (NoSuchElementException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
        model.addAttribute("message", "Получено поле");
        model.addAttribute("field", field);
        model.addAttribute("humus", fieldService.meanHumus(field));
        model.addAttribute("phosphorus", fieldService.meanPhosphorus(field));
        model.addAttribute("potassium", fieldService.meanPotassium(field));
        model.addAttribute("ph", fieldService.meanPH(field));
        model.addAttribute("density", fieldService.meanDensity(field));
        model.addAttribute("yield", fieldService.getPotentialYield(field));
        return "field";
    }

    @GetMapping(path = "/{id}/{yield}")
    @Operation(summary = "compute fertilizer rate", description = "Рассчитывает дозу NPK на планируемый урожай")
    public String getFertilizerRate(@PathVariable long id, @PathVariable double yield, Model model) {
        final Field field;
        try {
            field = fieldService.getFieldById(id);
        } catch (NoSuchElementException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
        model.addAttribute("message", "Рассчитана доза удобрений");
        model.addAttribute("field", field);
        model.addAttribute("nRate", fieldService.getNitrogenRate(field, yield));
        model.addAttribute("pRate", fieldService.getPhosphorusRate(field, yield));
        model.addAttribute("kRate", fieldService.getPotassiumRate(field, yield));
        return "rate";
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "delete field by ID", description = "Удаляет поле из базы данных по ID")
    public String deleteField(@PathVariable long id, Model model) {

        final Field field;
        try {
            field = fieldService.deleteField(id);
        } catch (NoSuchElementException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
        model.addAttribute("message", "Поле удалено");
        model.addAttribute("field", field);
        return "field";
    }
}
