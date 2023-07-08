package ru.practicum.category;

import ru.practicum.category.dto.CategoryDto;
import java.util.List;

public interface CategoryService {
    CategoryDto create(CategoryDto categoryDto);

    CategoryDto get(Long categoryId);

    CategoryDto update(Long categoryId, CategoryDto categoryDto);

    void delete(Long categoryId);

    List<CategoryDto> getAll(Integer from, Integer size);
}
