package ru.savrey.Sozinov_AV_diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.savrey.Sozinov_AV_diplom.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByLogin(String login);
    List<User> findByLastname(String lastname);
    User findByPhone(String phone);
    User findByEmail(String email);
}
