package ru.savrey.Sozinov_AV_diplom.api;

import lombok.Data;
import ru.savrey.Sozinov_AV_diplom.model.Farm;

@Data
public class FieldRequest {
    private final long farmId;
    private int area;
    private String soil;
    private String description;
}
