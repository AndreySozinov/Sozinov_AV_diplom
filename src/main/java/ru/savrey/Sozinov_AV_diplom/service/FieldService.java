package ru.savrey.Sozinov_AV_diplom.service;

import ru.savrey.Sozinov_AV_diplom.api.FieldRequest;
import ru.savrey.Sozinov_AV_diplom.model.Farm;
import ru.savrey.Sozinov_AV_diplom.model.Field;

import java.util.List;

public interface FieldService {
    Field createField(Field field);
    Field updateField(Long id, Field field);
    List<Field> getAllFieldsOnFarm(long farmId);
    Field getFieldById(Long id);
    Field deleteField(Long id);

    double meanHumus(long fieldId);

    double meanPhosphorus(long fieldId);

    double meanPotassium(long fieldId);


    double meanPH(long fieldId);

    double meanDensity(long fieldId);

    double getPotentialYield(long fieldId);

    double getNitrogenRate(long fieldId,double yield);
    double getPhosphorusRate(long fieldId, double yield);
    double getPotassiumRate(long fieldId, double yield);
}
