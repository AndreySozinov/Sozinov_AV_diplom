package ru.savrey.Sozinov_AV_diplom.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.savrey.Sozinov_AV_diplom.api.FarmRequest;
import ru.savrey.Sozinov_AV_diplom.model.Farm;
import ru.savrey.Sozinov_AV_diplom.repository.FarmRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class FarmServiceImpl implements FarmService{
    private final FarmRepository farmRepository;

    @Override
    public Farm createFarm(FarmRequest request) {
        if (farmRepository.findByTitle(request.getTitle()) != null) {
            throw new IllegalArgumentException("Хозяйство с таким названием уже есть.");
        }
        Farm farm = new Farm(request.getTitle());
        farm.setAddress(request.getAddress());
        return farmRepository.save(farm);
    }

    @Override
    public Farm updateFarm(Long id, FarmRequest request) {
        Farm existingFarm = getFarmById(id);
        if (existingFarm == null) {
            throw new IllegalArgumentException("{Хозяйства с таким ID не существует.");
        }
        existingFarm.setTitle(request.getTitle());
        existingFarm.setAddress(request.getAddress());
        return farmRepository.save(existingFarm);
    }

    @Override
    public List<Farm> getAllFarms() {
        return List.copyOf(farmRepository.findAll());
    }

    @Override
    public Farm getFarmById(Long id) {
        return farmRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Не найдено хозяйство с ID \"" + id + "\""));
    }

    @Override
    public Farm deleteFarm(Long id) {
        Farm farm = getFarmById(id);
        farmRepository.deleteById(id);
        return farm;
    }
}
