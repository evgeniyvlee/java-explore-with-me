package ru.practicum.server.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.StatsConstants;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.server.messages.LoggingMessages;
import ru.practicum.server.service.StatsService;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class StatsController {

    private final StatsService service;

    @PostMapping("/hit")
    public void createEndpointHit(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        log.debug(LoggingMessages.CREATE_ENDPOINT_HIT.toString());
        service.createEndpointHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> get(
            @RequestParam(name = "start", required = false) @DateTimeFormat(pattern = StatsConstants.DATE_TIME_PATTERN) LocalDateTime start,
            @RequestParam(name = "end", required = false) @DateTimeFormat(pattern = StatsConstants.DATE_TIME_PATTERN) LocalDateTime end,
            @RequestParam(name = "uris", required = false) List<String> uris,
            @RequestParam(name = "unique", defaultValue = "false") Boolean unique
    ) {
        log.debug(LoggingMessages.GET_VIEW_STATS.toString());
        System.out.println("/getViewStats");
        System.out.println("start = " + start + ", end = " + end + ", uris = " + uris + ", unique = " + unique);
        return service.getViewStats(start, end, uris, unique);
    }
}
