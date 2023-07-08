package ru.practicum.category;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.util.LoggingMessages;
import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path = "/admin/categories")
@Validated
public class AdminCategoryController {

    private final CategoryService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@Valid @RequestBody CategoryDto categoryDto) {
        log.debug(LoggingMessages.CREATE.toString(), categoryDto);
        return service.create(categoryDto);
    }

    @PatchMapping("{categoryId}")
    public CategoryDto update(@PathVariable Long categoryId, @Valid @RequestBody CategoryDto categoryDto) {
        log.debug(LoggingMessages.UPDATE.toString(), categoryDto);
        return service.update(categoryId, categoryDto);
    }

    @DeleteMapping("{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long categoryId) {
        log.debug(LoggingMessages.DELETE.toString(), categoryId);
        service.delete(categoryId);
    }
}
