package ru.savrey.Sozinov_AV_diplom.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
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
    @PreAuthorize("hasRole('ADMIN')")
    public Farm createFarm(Farm newFarm) {
        if (farmRepository.findByTitle(newFarm.getTitle()) != null) {
            throw new IllegalArgumentException("Хозяйство с таким названием уже есть.");
        }
        Farm farm = new Farm(newFarm.getUser(), newFarm.getTitle());
        farm.setAddress(newFarm.getAddress());
        return farmRepository.save(farm);
    }

    @Override
    public Farm updateFarm(Long id, Farm editedFarm) {
        Farm existingFarm = getFarmById(id);
        if (existingFarm == null) {
            throw new IllegalArgumentException("{Хозяйства с таким ID не существует.");
        }
        existingFarm.setTitle(editedFarm.getTitle());
        existingFarm.setAddress(editedFarm.getAddress());
        return farmRepository.save(existingFarm);
    }

    @Override
    public List<Farm> getAllFarms() {
        return List.copyOf(farmRepository.findAll());
    }

    @Override
    @PostAuthorize("hasRole('ADMIN') || " + "returnObject.user.login == authentication.name")
    public Farm getFarmById(Long id) {
        return farmRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Не найдено хозяйство с ID \"" + id + "\""));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public Farm deleteFarm(Long id) {
        Farm farm = getFarmById(id);
        farmRepository.deleteById(id);
        return farm;
    }
}
