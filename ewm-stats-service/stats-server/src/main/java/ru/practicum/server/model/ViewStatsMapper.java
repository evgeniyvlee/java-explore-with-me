package ru.practicum.server.model;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.ViewStatsDto;

@UtilityClass
public class ViewStatsMapper {

    public ViewStatsDto toViewStatsDto(final ViewStats viewStats) {
        return new ViewStatsDto(viewStats.getApp(), viewStats.getUri(), viewStats.getHits());
    }
}
