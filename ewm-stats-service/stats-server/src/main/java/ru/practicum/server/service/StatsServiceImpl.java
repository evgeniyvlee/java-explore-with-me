package ru.practicum.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.server.model.EndpointHit;
import ru.practicum.server.model.EndpointHitMapper;
import ru.practicum.server.model.ViewStats;
import ru.practicum.server.model.ViewStatsMapper;
import ru.practicum.server.repository.StatsRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Transactional(readOnly = true)
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

    @Transactional
    @Override
    public void createEndpointHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = statsRepository.save(EndpointHitMapper.toEndpointHit(endpointHitDto));
        System.out.println("endpointHitDto = " + endpointHitDto);
    }
}

