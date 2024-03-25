package ru.savrey.Sozinov_AV_diplom.service;

import ru.savrey.Sozinov_AV_diplom.api.PointRequest;
import ru.savrey.Sozinov_AV_diplom.model.Field;
import ru.savrey.Sozinov_AV_diplom.model.Point;

import java.util.List;

public interface PointService {
    Point createPoint(PointRequest request);
    Point updatePoint(Long id, PointRequest request);
    List<Point> getAllPointsOnField(Field field);
    Point getPointById(Long id);
    Point deletePoint(Long id);
}
