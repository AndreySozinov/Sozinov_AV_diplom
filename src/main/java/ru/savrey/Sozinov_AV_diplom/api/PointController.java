package ru.savrey.Sozinov_AV_diplom.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.savrey.Sozinov_AV_diplom.model.Field;
import ru.savrey.Sozinov_AV_diplom.model.Point;
import ru.savrey.Sozinov_AV_diplom.service.PointService;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Controller
@RequestMapping("/api/point/")
@Tag(name = "Point")
public class PointController {

    @Autowired
    private PointService pointService;

    @PostMapping
    @Operation(summary = "create new point", description = "Добавляет новую точку")
    public String createPoint(@RequestBody PointRequest request, Model model) {
        final Point point;
        try {
            point = pointService.createPoint(request);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
        model.addAttribute("message", "Добавлена новая точка");
        model.addAttribute("point", point);
        return "point";
    }

    @PutMapping(path = "/{id}")
    @Operation(summary = "update point", description = "Редактирует точку")
    public String updatePoint(@PathVariable long id, @RequestBody PointRequest request, Model model) {
        final Point point;
        try {
            point = pointService.updatePoint(id, request);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
        model.addAttribute("message", "Точка обновлена");
        model.addAttribute("point", point);
        return "point";
    }

    @GetMapping(path = "/all/{field}")
    @Operation(summary = "get all points on field list", description = "Загружает список всех точек на поле из БД")
    public String getAllPoints(@PathVariable Field field, Model model) {
        final List<Point> points;
        try {
            points = pointService.getAllPointsOnField(field);
        } catch (RuntimeException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
        model.addAttribute("message", "Получен список точек");
        model.addAttribute("points", points);
        return "points";
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "get point by ID", description = "Загружает точку из базы данных по ID")
    public String getPointInfo(@PathVariable long id, Model model) {
        final Point point;
        try {
            point = pointService.getPointById(id);
        } catch (NoSuchElementException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
        model.addAttribute("message", "Получена точка");
        model.addAttribute("point", point);
        return "point";
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "delete point by ID", description = "Удаляет точку из базы данных по ID")
    public String deletePoint(@PathVariable long id, Model model) {
        final Point point;
        try {
            point = pointService.deletePoint(id);
        } catch (NoSuchElementException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
        model.addAttribute("message", "Точка удалена");
        model.addAttribute("point", point);
        return "point";
    }
}
