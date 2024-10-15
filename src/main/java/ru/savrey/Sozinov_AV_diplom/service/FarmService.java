package ru.savrey.Sozinov_AV_diplom.service;

import org.springframework.stereotype.Service;
import ru.savrey.Sozinov_AV_diplom.api.FarmRequest;
import ru.savrey.Sozinov_AV_diplom.model.Farm;

import java.util.List;


public interface FarmService {

    Farm createFarm(Farm request);
    Farm updateFarm(Long id, Farm request);
    List<Farm> getAllFarms();
    Farm getFarmById(Long id);
    Farm deleteFarm(Long id);

}
