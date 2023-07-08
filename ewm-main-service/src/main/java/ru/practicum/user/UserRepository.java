package ru.practicum.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.user.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Sort SORT_USER_ID_ASC = Sort.by(Sort.Direction.ASC, "id");

    List<User> findAllByIdIn(Long[] userIds, Pageable pageable);

    Optional<User> findByName(String name);
}
