package ru.practicum.compilation;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import java.util.List;

public interface CompilationService {
    CompilationDto create(NewCompilationDto compilationDto);

    CompilationDto update(Long compId, UpdateCompilationRequest updatedCompilation);

    void delete(Long compId);

    CompilationDto getById(Long compId);

    List<CompilationDto> get(Boolean pinned, Integer from, Integer size);
}
