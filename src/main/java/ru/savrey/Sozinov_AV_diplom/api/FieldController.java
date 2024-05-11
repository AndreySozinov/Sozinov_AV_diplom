package ru.savrey.Sozinov_AV_diplom.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.savrey.Sozinov_AV_diplom.model.Farm;
import ru.savrey.Sozinov_AV_diplom.model.Field;
import ru.savrey.Sozinov_AV_diplom.service.FarmService;
import ru.savrey.Sozinov_AV_diplom.service.FieldService;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Controller
@RequestMapping("/api/field")
@Tag(name = "Field")
public class FieldController {

    @Autowired
    private FieldService fieldService;

    @Autowired
    private FarmService farmService;

    //region Добавление нового поля
    @PostMapping
    @Operation(summary = "create new field", description = "Добавляет новое поле")
    public String createField(@RequestBody FieldRequest request, Model model) {
        final Field fieldIncoming = fieldFromFarmIdToFarm(request, farmService);
        Field fieldOutcoming = null;

        try {
            fieldOutcoming = fieldService.createField(fieldIncoming);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("message", "Не сохранилось поле" + ex.getMessage());
            return "error";
        } catch (Exception ex) {
            model.addAttribute("message", "Неизв. ощибка" + ex.getMessage());
        }
        model.addAttribute("message", "Добавлено новое поле");
        model.addAttribute("field", fieldOutcoming);
        return "field/field";
    }
    //endregion

    //region Редактирование поля
    @PostMapping(path = "/update/{id}")
    @Operation(summary = "update field", description = "Редактирует поле")
    public String updateField(@PathVariable long id, @RequestBody FieldRequest request, Model model) {
        final Field fieldIncoming = fieldFromFarmIdToFarm(request, farmService);
        Field fieldOutcoming;
        try {
            fieldOutcoming = fieldService.updateField(id, fieldIncoming);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
        model.addAttribute("message", "Поле изменено");
        model.addAttribute("field", fieldOutcoming);
        return "field/field";
    }
    //endregion

    //region Список полей отдельного хозяйства в БД
    @GetMapping(path = "/all/{farmId}")
    @Operation(summary = "get all fields list at farm", description = "Загружает список всех полей в хозяйстве из БД")
    public String getAllFields(@PathVariable long farmId, Model model) {
        final List<Field> fields;
        final Farm farm;

        try {
            farm = farmService.getFarmById(farmId);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }

        try {
            fields = fieldService.getAllFieldsOnFarm(farmId);
        } catch (RuntimeException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
        model.addAttribute("message", "Получен список полей");
        model.addAttribute("farm", farm);
        model.addAttribute("fields", fields);
        return "field/fields";
    }
    //endregion

    //region Загрузка поля по ID из БД
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
        model.addAttribute("humus", fieldService.meanHumus(id));
        model.addAttribute("phosphorus", fieldService.meanPhosphorus(id));
        model.addAttribute("potassium", fieldService.meanPotassium(id));
        model.addAttribute("ph", fieldService.meanPH(id));
        model.addAttribute("density", fieldService.meanDensity(id));
        model.addAttribute("yield", fieldService.getPotentialYield(id));
        return "field/field";
    }
    //endregion

    //region Расчет дозы NPK
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
        model.addAttribute("nRate", fieldService.getNitrogenRate(id, yield));
        model.addAttribute("pRate", fieldService.getPhosphorusRate(id, yield));
        model.addAttribute("kRate", fieldService.getPotassiumRate(id, yield));
        return "rate";
    }
    //endregion

    //region Удаление поля из БД
    @GetMapping(path = "/delete/{id}")
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
        return "field/field";
    }
    //endregion


    /**
     * Заменяет поле farmId объекта FieldRequest на поле Farm в объекте Field
     * @param fieldRequest объект класса FieldRequest с полем farmId типа long
     * @param service объект класса FarmService
     * @return объект Field, готовый для сохранения в депозитории
     */
    private Field fieldFromFarmIdToFarm(FieldRequest fieldRequest, FarmService service) {
        final Farm farm;
        try {
            farm = service.getFarmById(fieldRequest.getFarmId());
            System.out.println("Получили хозяйство с ID: " + farm.getFarmId());
        } catch (IllegalArgumentException ex) {
            System.out.println("Не удалось найти хозяйство с таким farmId " + ex.getMessage());
            return null;
        }

        Field field = new Field(farm, fieldRequest.getArea());
        field.setSoil(fieldRequest.getSoil());
        field.setDescription(fieldRequest.getDescription());
        return field;
    }
}


