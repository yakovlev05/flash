package ru.yakovlev05.school.flash.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yakovlev05.school.flash.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    List<User> findByIdIn(List<Long> ids);
}
