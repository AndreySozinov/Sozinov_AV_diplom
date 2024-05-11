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
import ru.savrey.Sozinov_AV_diplom.service.FarmService;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Controller
@RequestMapping("/api/farm")
@Tag(name = "Farm")
public class FarmController {

    @Autowired
    private FarmService farmService;

    //region Добавление нового хозяйства
    @PostMapping
    @Operation(summary = "create new farm", description = "Добавляет новое хозяйство")
    public String createFarm(@RequestBody FarmRequest request, Model model) {
        final Farm farm;
        try {
            farm = farmService.createFarm(request);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
        model.addAttribute("message", "Хозяйство добавлено");
        model.addAttribute("farm", farm);
        return "farm/farm";
    }
    //endregion

    //region Редактирование хозяйства
    @PostMapping(path = "/update/{id}")
    @Operation(summary = "update farm", description = "Редактирует хозяйство")
    public String updateFarm(@PathVariable long id, @RequestBody FarmRequest request, Model model) {
        final Farm farm;
        try {
            farm = farmService.updateFarm(id, request);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
        model.addAttribute("message", "Хозяйство обновлено");
        model.addAttribute("farm", farm);
        return "farm/farm";
    }
    //endregion

    //region Список хозяйств в БД
    @GetMapping(path = "/all")
    @Operation(summary = "get all farms list", description = "Загружает список всех хозяйств из БД")
    public String getAllFarms(Model model) {
        final List<Farm> farms;
        try {
            farms = farmService.getAllFarms();
        } catch (RuntimeException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
        model.addAttribute("message", "Получен список хозяйств");
        model.addAttribute("farms", farms);
        return "farm/farms";
    }
    //endregion

    //region Загрузка хозяйства по ID из БД
    @GetMapping(path = "/{id}")
    @Operation(summary = "get farm by ID", description = "Загружает хозяйство из базы данных по ID")
    public String getFarmInfo(@PathVariable long id, Model model) {
        final Farm farm;
        try {
            farm = farmService.getFarmById(id);
        } catch (NoSuchElementException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
        model.addAttribute("message", "Найдено хозяйство");
        model.addAttribute("farm", farm);
        return "farm/farm";
    }
    //endregion

    //region Удаление хозяйства из БД
    @GetMapping(path = "/delete/{id}")
    @Operation(summary = "delete farm by ID", description = "Удаляет хозяйство из базы данных по ID")
    public String deleteFarm(@PathVariable long id, Model model) {
        final Farm farm;
        try {
            farm = farmService.deleteFarm(id);
        } catch (NoSuchElementException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
        model.addAttribute("message", "Хозяйство удалено");
        model.addAttribute("farm", farm);
        return "farm/farm";
    }
    //endregion
}
