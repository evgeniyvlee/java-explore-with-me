package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.exception.ExceptionMessages;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {

    private List<Long> events = new ArrayList<>();

    private Boolean pinned = Boolean.FALSE;

    @NotBlank
    @Size(min = 1, message = ExceptionMessages.COMPILATION_TITLE_TOO_SHORT)
    @Size(max = 50, message = ExceptionMessages.COMPILATION_TITLE_TOO_LONG)
    private String title;
}
