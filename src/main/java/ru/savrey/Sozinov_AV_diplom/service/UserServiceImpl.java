package ru.savrey.Sozinov_AV_diplom.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.savrey.Sozinov_AV_diplom.api.UserRequest;
import ru.savrey.Sozinov_AV_diplom.model.Farm;
import ru.savrey.Sozinov_AV_diplom.model.User;
import ru.savrey.Sozinov_AV_diplom.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepo) {
        return username -> {
            User user = userRepo.findByLogin(username);
            if (user != null) return user;

            throw new UsernameNotFoundException("User '" + username + "' not found");
        };
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public User createUser(UserRequest request) {
        if (userRepository.findByLogin(request.getLogin()) != null) {
            throw new IllegalArgumentException("Пользователь с таким логином уже есть.");
        } else if (userRepository.findByPhone(request.getPhone()) != null) {
            throw new IllegalArgumentException("Пользователь с таким телефоном уже есть.");
        } else if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new IllegalArgumentException("Пользователь с таким email уже есть.");
        }
        User user = new User(request.getLastName(), request.getLogin(), request.getPassword());
        user.setFirstname(request.getFirstName());
        user.setPatronymic(request.getPatronymic());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, UserRequest request) {
        User existingUser = getUserById(id);
        if (existingUser == null) {
            throw new IllegalArgumentException("{Пользователя с таким ID не существует.");
        }
        existingUser.setFirstname(request.getFirstName());
        existingUser.setLastname(request.getLastName());
        existingUser.setPatronymic(request.getPatronymic());
        existingUser.setPhone(request.getPhone());
        existingUser.setEmail(request.getEmail());
        existingUser.setLogin(request.getLogin());
        existingUser.setPassword(request.getPassword());
        return userRepository.save(existingUser);
    }

    @Override
    public List<User> getAllUsers() {
        return List.copyOf(userRepository.findAll());
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Не найден пользователь с ID \"" + id + "\""));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public User deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.deleteById(id);
        return user;
    }
}
