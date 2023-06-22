package ru.practicum.server.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.dto.ViewStatsDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ViewStatsMapper {

    public static ViewStatsDto toViewStatsDto(final ViewStats viewStats) {
        return new ViewStatsDto(viewStats.getApp(), viewStats.getUri(), viewStats.getHits());
    }
}
