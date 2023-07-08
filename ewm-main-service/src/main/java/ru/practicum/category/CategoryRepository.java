package ru.practicum.category;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.category.model.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Sort SORT_CATEGORY_ID_DESC = Sort.by(Sort.Direction.DESC, "id");

    List<Category> findByIdIn(List<Long> ids);

    Optional<Category> findByName(String name);
}
