package ru.savrey.Sozinov_AV_diplom.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.savrey.Sozinov_AV_diplom.model.Field;

@Repository
public interface FieldRepository extends JpaRepository<Field, Long> {

    @Override
    Page<Field> findAll(Pageable pageable);

}
