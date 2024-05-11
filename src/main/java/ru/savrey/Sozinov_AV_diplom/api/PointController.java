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
import ru.savrey.Sozinov_AV_diplom.model.Point;
import ru.savrey.Sozinov_AV_diplom.service.FarmService;
import ru.savrey.Sozinov_AV_diplom.service.FieldService;
import ru.savrey.Sozinov_AV_diplom.service.PointService;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Controller
@RequestMapping("/api/point")
@Tag(name = "Point")
public class PointController {

    @Autowired
    private PointService pointService;

    @Autowired
    private FieldService fieldService;

    //region Добавление новой точки
    @PostMapping
    @Operation(summary = "create new point", description = "Добавляет новую точку")
    public String createPoint(@RequestBody PointRequest request, Model model) {
        final Point pointIncoming = pointFromFieldIdToField(request, fieldService);
        Point pointOutcoming;
        try {
            pointOutcoming = pointService.createPoint(pointIncoming);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
        model.addAttribute("message", "Добавлена новая точка");
        model.addAttribute("point", pointOutcoming);
        return "point/point";
    }
    //endregion

    //region Редактирование точки
    @PostMapping(path = "/update/{id}")
    @Operation(summary = "update point", description = "Редактирует точку")
    public String updatePoint(@PathVariable long id, @RequestBody PointRequest request, Model model) {
        final Point pointIncoming = pointFromFieldIdToField(request, fieldService);
        Point pointOutcoming;
        try {
            pointOutcoming = pointService.updatePoint(id, pointIncoming);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
        model.addAttribute("message", "Точка обновлена");
        model.addAttribute("point", pointOutcoming);
        return "point/point";
    }
    //endregion

    //region Список точек отдельного поля в БД
    @GetMapping(path = "/all/{fieldId}")
    @Operation(summary = "get all points on field list", description = "Загружает список всех точек на поле из БД")
    public String getAllPoints(@PathVariable long fieldId, Model model) {
        final List<Point> points;
        final Field field;
        try {
            field = fieldService.getFieldById(fieldId);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }

        try {
            points = pointService.getAllPointsOnField(fieldId);
        } catch (RuntimeException ex) {
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
        model.addAttribute("message", "Получен список точек");
        model.addAttribute("field", field);
        model.addAttribute("points", points);
        return "point/points";
    }
    //endregion

    //region Загрузка точки по ID из БД
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
        return "point/point";
    }
    //endregion

    //region Удаление точки из БД
    @GetMapping(path = "/delete/{id}")
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
        return "point/point";
    }
    //endregion

    /**
     * Заменяет поле fieldId объекта PointRequest на поле Field в объекте Point
     * @param pointRequest объект класса PointRequest с полем fieldId типа long
     * @param service объект класса FieldService
     * @return объект Point, готовый для сохранения в депозитории
     */
    private Point pointFromFieldIdToField(PointRequest pointRequest, FieldService service) {
        final Field field;
        try {
            field = service.getFieldById(pointRequest.getFieldId());
            System.out.println("Получили поле с ID: " + field.getFieldId());
        } catch (IllegalArgumentException ex) {
            System.out.println("Не удалось заменить fieldId на Field в объекте Point " + ex.getMessage());
            return null;
        }

        return getPoint(pointRequest, field);
    }

    private static Point getPoint(PointRequest pointRequest, Field field) {
        LocalDate localDateInFinalObject = LocalDate.parse(pointRequest.getSampled_in());
        Point point = new Point(field,
                pointRequest.getLatitude(),
                pointRequest.getLongitude(),
                localDateInFinalObject);
        point.setHumus(pointRequest.getHumus());
        point.setPhosphorus(pointRequest.getPhosphorus());
        point.setPotassium(pointRequest.getPotassium());
        point.setPh(pointRequest.getPh());
        point.setDensity(pointRequest.getDensity());
        return point;
    }
}
