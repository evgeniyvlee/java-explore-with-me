package ru.practicum.category;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.util.LoggingMessages;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path = "/categories")
@Validated
public class PublicCategoryController {

    private final CategoryService service;

    @GetMapping("{categoryId}")
    public CategoryDto get(@PathVariable Long categoryId) {
        log.debug(LoggingMessages.GET.toString(), categoryId);
        return service.get(categoryId);
    }

    @GetMapping
    public List<CategoryDto> getAll(@RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                    @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.debug(LoggingMessages.GET_ALL.toString());
        return service.getAll(from, size);
    }
}
