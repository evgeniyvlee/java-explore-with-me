package ru.practicum.server.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.server.model.EndpointHitMapper;
import ru.practicum.server.model.ViewStats;
import ru.practicum.server.model.ViewStatsMapper;
import ru.practicum.server.repository.StatsRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    public List<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        List<ViewStats> viewStatsList;
        if (unique) {
            viewStatsList = statsRepository.getViewStatsWithUniqueIp(start, end, uris);
        } else {
            viewStatsList = statsRepository.getViewStatsWithAllIp(start, end, uris);
        }
        return viewStatsList.stream().map(ViewStatsMapper::toViewStatsDto).collect(Collectors.toList());
    }

    @Override
    public void createEndpointHit(EndpointHitDto endpointHitDto) {
        statsRepository.save(EndpointHitMapper.toEndpointHit(endpointHitDto));
    }
}

