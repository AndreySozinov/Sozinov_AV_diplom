package ru.savrey.Sozinov_AV_diplom.service;

import ru.savrey.Sozinov_AV_diplom.api.FarmRequest;
import ru.savrey.Sozinov_AV_diplom.model.Farm;

import java.util.List;

public interface FarmService {

    Farm createFarm(FarmRequest request);
    Farm updateFarm(Long id, FarmRequest request);
    List<Farm> getAllFarms();
    Farm getFarmById(Long id);
    Farm deleteFarm(Long id);

}
