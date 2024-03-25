package ru.savrey.Sozinov_AV_diplom.service;

import ru.savrey.Sozinov_AV_diplom.api.FieldRequest;
import ru.savrey.Sozinov_AV_diplom.model.Farm;
import ru.savrey.Sozinov_AV_diplom.model.Field;

import java.util.List;

public interface FieldService {
    Field createField(FieldRequest request);
    Field updateField(Long id, FieldRequest request);
    List<Field> getAllFieldsOnFarm(Farm farm);
    Field getFieldById(Long id);
    Field deleteField(Long id);

    double meanHumus(Field field);

    double meanPhosphorus(Field field);

    double meanPotassium(Field field);


    double meanPH(Field field);

    double meanDensity(Field field);

    double getPotentialYield(Field field);

    double getNitrogenRate(Field field,double yield);
    double getPhosphorusRate(Field field, double yield);
    double getPotassiumRate(Field field, double yield);
}
