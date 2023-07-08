package ru.practicum.stat;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface StatsService {
    void createHit(HttpServletRequest request);

    Map<Long, Long> getViews(LocalDateTime start, LocalDateTime end, List<Long> ids, Boolean unique);
}
