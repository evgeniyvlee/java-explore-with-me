package ru.practicum.compilation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.util.LoggingMessages;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/compilations")
public class PublicCompilationController {

    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> get(@RequestParam(value = "pinned", required = false) Boolean pinned,
                                    @RequestParam(name = "from", defaultValue = "0") Integer from,
                                    @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.debug(LoggingMessages.GET_ALL.toString());
        return compilationService.get(pinned, from, size);
    }

    @GetMapping("{compId}")
    public CompilationDto getById(@PathVariable Long compId) {
        log.debug(LoggingMessages.GET.toString(), compId);
        return compilationService.getById(compId);
    }
}
