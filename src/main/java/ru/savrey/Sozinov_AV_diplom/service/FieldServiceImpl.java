package ru.savrey.Sozinov_AV_diplom.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.savrey.Sozinov_AV_diplom.api.FieldRequest;
import ru.savrey.Sozinov_AV_diplom.model.Farm;
import ru.savrey.Sozinov_AV_diplom.model.Field;
import ru.savrey.Sozinov_AV_diplom.model.Point;
import ru.savrey.Sozinov_AV_diplom.repository.FieldRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class FieldServiceImpl implements FieldService{
    private final FieldRepository fieldRepository;
    private final PointServiceImpl pointService;

    @Value("${application.soil-layer-thickness:}")
    private double layerThickness;

    @Value("${application.correction-coefficient.crop-residue-humification:}")
    private double humificationCoeff;

    @Value("${application.correction-coefficient.particle-size-distribution:}")
    private double particleSizeCoeff;

    @Value("${application.nitrogen-content-in-humus:}")
    private double nitrogenInHumus;

    @Value("${application.recovery-coefficient.soil.nitrogen:}")
    private double soilNitrogenCoeff;

    @Value("${application.recovery-coefficient.soil.phosphorus:}")
    private double soilPhosphorusCoeff;

    @Value("${application.recovery-coefficient.soil.potassium:}")
    private double soilPotassiumCoeff;

    @Value("${application.recovery-coefficient.fertilizer.nitrogen:}")
    private double fertilizerNitrogenCoeff;

    @Value("${application.recovery-coefficient.fertilizer.phosphorus:}")
    private double fertilizerPhosphorusCoeff;

    @Value("${application.recovery-coefficient.fertilizer.potassium:}")
    private double fertilizerPotassiumCoeff;

    @Value("${application.recovery-coefficient.yield-removal.nitrogen:}")
    private double nitrogenYieldRemoval;

    @Value("${application.recovery-coefficient.yield-removal.phosphorus:}")
    private double phosphorusYieldRemoval;

    @Value("${application.recovery-coefficient.yield-removal.potassium:}")
    private double potassiumYieldRemoval;

    @Override
    public Field createField(FieldRequest request) {
        Field field = new Field(request.getFarm(), request.getArea());
        field.setSoil(request.getSoil());
        field.setDescription(request.getDescription());
        return fieldRepository.save(field);
    }

    @Override
    public Field updateField(Long id, FieldRequest request) {
        Field existingField = getFieldById(id);
        if (existingField == null) {
            throw new IllegalArgumentException("Поля с таким ID не существует.");
        }
        existingField.setArea(request.getArea());
        existingField.setSoil(request.getSoil());
        existingField.setDescription(request.getDescription());
        return fieldRepository.save(existingField);
    }

    @Override
    public List<Field> getAllFieldsOnFarm(Farm farm) {
        return List.copyOf(fieldRepository.findAll()
                .stream()
                .filter(it -> it.getFarm() == farm)
                .toList());
    }

    @Override
    public Field getFieldById(Long id) {
        return fieldRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Не найдено поле с ID \"" + id + "\""));
    }

    @Override
    public Field deleteField(Long id) {
        Field field = getFieldById(id);
        fieldRepository.deleteById(id);
        return field;
    }

    @Override
    public double meanHumus(Field field) {
        return pointService.getAllPointsOnField(field).stream()
                .mapToDouble(Point::getHumus)
                .average()
                .orElse(Double.NaN);
    }

    @Override
    public double meanPhosphorus(Field field) {
        return pointService.getAllPointsOnField(field).stream()
                .mapToDouble(Point::getPhosphorus)
                .average()
                .orElse(Double.NaN);
    }

    @Override
    public double meanPotassium(Field field) {
        return pointService.getAllPointsOnField(field).stream()
                .mapToDouble(Point::getPotassium)
                .average()
                .orElse(Double.NaN);
    }

    @Override
    public double meanPH(Field field) {
        return pointService.getAllPointsOnField(field).stream()
                .mapToDouble(Point::getPH)
                .average()
                .orElse(Double.NaN);
    }

    @Override
    public double meanDensity(Field field) {
        return pointService.getAllPointsOnField(field).stream()
                .mapToDouble(Point::getDensity)
                .average()
                .orElse(Double.NaN);
    }

    @Override
    public double getPotentialYield(Field field) {
        double nitrogenStock = meanHumus(field)
                * layerThickness
                * meanDensity(field)
                * humificationCoeff
                * particleSizeCoeff
                * nitrogenInHumus
                * 10_000;
        double phosphorusStock = meanPhosphorus(field) * layerThickness * meanDensity(field) * 10;
        double potassiumStock = meanPotassium(field) * layerThickness * meanDensity(field) * 10;

        double yieldByN = nitrogenStock * soilNitrogenCoeff / (nitrogenYieldRemoval * 100);
        double yieldByP = phosphorusStock * soilPhosphorusCoeff / (phosphorusYieldRemoval * 100);
        double yieldByK = potassiumStock * soilPotassiumCoeff / (potassiumYieldRemoval * 100);

        return Math.min(yieldByN, Math.min(yieldByP, yieldByK));
    }

    @Override
    public double getNitrogenRate(Field field, double yield) {
        double yieldIncrease = yield - getPotentialYield(field);
        if (yieldIncrease <= 0){
            throw new IllegalArgumentException("Азотные удобрения не требуются.");
        }
        return yieldIncrease * nitrogenYieldRemoval * 100 / fertilizerNitrogenCoeff;
    }

    @Override
    public double getPhosphorusRate(Field field, double yield) {
        double yieldIncrease = yield - getPotentialYield(field);
        if (yieldIncrease <= 0){
            throw new IllegalArgumentException("Фосфорные удобрения не требуются.");
        }
        return yieldIncrease * phosphorusYieldRemoval * 100 / fertilizerPhosphorusCoeff;
    }

    @Override
    public double getPotassiumRate(Field field, double yield) {
        double yieldIncrease = yield - getPotentialYield(field);
        if (yieldIncrease <= 0){
            throw new IllegalArgumentException("Калийные удобрения не требуются.");
        }
        return yieldIncrease * potassiumYieldRemoval * 100 / fertilizerPotassiumCoeff;
    }
}
