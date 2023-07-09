package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.event.EventRepository;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.exception.ExceptionMessages;
import ru.practicum.exception.ValidationException;
import ru.practicum.util.PageSettings;
import ru.practicum.util.SortSettings;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final EventRepository eventRepository;

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        validate(categoryDto);
        Category category = categoryRepository.save(CategoryMapper.toCategory(categoryDto));
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto get(Long categoryId) {
        return CategoryMapper.toCategoryDto(getById(categoryId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getAll(Integer from, Integer size) {
        Pageable pageable = new PageSettings(from, size, SortSettings.SORT_ID_DESC);
        return categoryRepository.findAll(pageable).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto update(Long categoryId, CategoryDto categoryDto) {
        Category category = getById(categoryId);
        if (!category.getName().equals(categoryDto.getName())) {
            validate(categoryDto);
            category.setName(categoryDto.getName());
        }
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public void delete(Long categoryId) {
        getById(categoryId);
        validateCategoryForDeleting(categoryId);
        categoryRepository.deleteById(categoryId);
    }

    private void validateCategoryForDeleting(Long categoryId) {
        if (eventRepository.findByCategoryId(categoryId).isPresent()) {
            throw new ValidationException(ExceptionMessages.CATEGORY_IS_NOT_EMPTY);
        }
    }

    private Category getById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.CATEGORY_NOT_FOUND));
    }

    private void validate(CategoryDto categoryDto) {
        if (categoryRepository.findByName(categoryDto.getName()).isPresent()) {
            throw new ValidationException(ExceptionMessages.CATEGORY_NAME_CONFLICT);
        }
    }
}
