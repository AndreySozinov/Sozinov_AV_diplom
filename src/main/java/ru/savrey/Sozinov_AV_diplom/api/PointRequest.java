package ru.savrey.Sozinov_AV_diplom.api;

import lombok.Data;
import ru.savrey.Sozinov_AV_diplom.model.Field;

@Data
public class PointRequest {
    private final Field field;
    private double latitude;
    private double longitude;
    private double humus;
    private double phosphorus;
    private int potassium;
    private double pH;
    private double density;
}
