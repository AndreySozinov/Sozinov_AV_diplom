package ru.savrey.Sozinov_AV_diplom.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.savrey.Sozinov_AV_diplom.api.PointRequest;
import ru.savrey.Sozinov_AV_diplom.model.Field;
import ru.savrey.Sozinov_AV_diplom.model.Point;
import ru.savrey.Sozinov_AV_diplom.repository.PointRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService{
    private final PointRepository pointRepository;

    @Override
    public Point createPoint(PointRequest request) {
        Point point = new Point(request.getField(), request.getLatitude(), request.getLongitude());
        point.setHumus(request.getHumus());
        point.setPhosphorus(request.getPhosphorus());
        point.setPotassium(request.getPotassium());
        point.setPH(request.getPH());
        point.setDensity(request.getDensity());
        return pointRepository.save(point);
    }

    @Override
    public Point updatePoint(Long id, PointRequest request) {
        Point existingPoint = getPointById(id);
        if (existingPoint == null) {
            throw new IllegalArgumentException("Точки с таким ID не существует.");
        }
        existingPoint.setLatitude(request.getLatitude());
        existingPoint.setLongitude(request.getLongitude());
        existingPoint.setHumus(request.getHumus());
        existingPoint.setPhosphorus(request.getPhosphorus());
        existingPoint.setPotassium(request.getPotassium());
        existingPoint.setPH(request.getPH());
        existingPoint.setDensity(request.getDensity());
        return pointRepository.save(existingPoint);
    }

    @Override
    public List<Point> getAllPointsOnField(Field field) {
        return List.copyOf(pointRepository.findAll()
                .stream()
                .filter(it -> it.getField() == field)
                .toList());
    }

    @Override
    public Point getPointById(Long id) {
        return pointRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Не найдена точка с ID \"" + id + "\""));
    }

    @Override
    public Point deletePoint(Long id) {
        Point point = getPointById(id);
        pointRepository.deleteById(id);
        return point;
    }
}
