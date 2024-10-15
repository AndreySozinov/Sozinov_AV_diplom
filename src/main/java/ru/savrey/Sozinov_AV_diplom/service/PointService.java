package ru.savrey.Sozinov_AV_diplom.service;

import org.springframework.data.domain.Pageable;
import ru.savrey.Sozinov_AV_diplom.api.PointRequest;
import ru.savrey.Sozinov_AV_diplom.model.Field;
import ru.savrey.Sozinov_AV_diplom.model.Point;

import java.util.Collection;
import java.util.List;

public interface PointService {
    Point createPoint(Point request);
    Point updatePoint(Long id, Point request);
    List<Point> getAllPointsOnField(long fieldId);
    Point getPointById(Long id);
    Point deletePoint(Long id);

}
