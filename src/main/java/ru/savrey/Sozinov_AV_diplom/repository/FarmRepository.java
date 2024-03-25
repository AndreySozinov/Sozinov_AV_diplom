package ru.savrey.Sozinov_AV_diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.savrey.Sozinov_AV_diplom.model.Farm;

@Repository
public interface FarmRepository extends JpaRepository<Farm, Long> {

    Farm findByTitle(String title);
}
