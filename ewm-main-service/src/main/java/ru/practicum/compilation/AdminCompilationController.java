package ru.practicum.compilation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.util.LoggingMessages;
import javax.validation.Valid;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/admin/compilations")
@Validated
public class AdminCompilationController {

    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(@Valid @RequestBody NewCompilationDto compilationDto) {
        log.debug(LoggingMessages.CREATE.toString(), compilationDto);
        return compilationService.create(compilationDto);
    }

    @PatchMapping("{compId}")
    public CompilationDto update(@PathVariable Long compId,
                                 @Valid @RequestBody UpdateCompilationRequest updatedCompilation) {
        log.debug(LoggingMessages.UPDATE.toString(), compId);
        return compilationService.update(compId, updatedCompilation);
    }

    @DeleteMapping("{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long compId) {
        log.debug(LoggingMessages.DELETE.toString(), compId);
        compilationService.delete(compId);
    }
}
