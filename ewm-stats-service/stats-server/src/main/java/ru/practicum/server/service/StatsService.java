package ru.practicum.server.service;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    List<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

    void createEndpointHit(EndpointHitDto endpointHitDto);
}
