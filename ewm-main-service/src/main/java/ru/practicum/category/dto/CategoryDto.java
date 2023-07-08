package ru.practicum.category.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.exception.ExceptionMessages;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Long id;

    @NotBlank
    @Size(min = 1, message = ExceptionMessages.CATEGORY_NAME_TOO_SHORT)
    @Size(max = 50, message = ExceptionMessages.CATEGORY_NAME_TOO_LONG)
    private String name;
}
