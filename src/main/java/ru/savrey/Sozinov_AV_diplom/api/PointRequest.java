package ru.savrey.Sozinov_AV_diplom.api;

import lombok.Data;
import ru.savrey.Sozinov_AV_diplom.model.Field;

import java.util.Date;

@Data
public class PointRequest {
    private final long fieldId;
    private double latitude;
    private double longitude;
    private double humus;
    private double phosphorus;
    private int potassium;
    private double ph;
    private double density;
    private String sampled_in;
}
