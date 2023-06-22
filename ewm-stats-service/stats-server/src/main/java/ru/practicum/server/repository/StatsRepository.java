package ru.practicum.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.server.model.EndpointHit;
import ru.practicum.server.model.ViewStats;
import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query(
        "SELECT new ru.practicum.server.model.ViewStats(hit.app, hit.uri, count(distinct hit.ip)) " +
        "FROM EndpointHit hit " +
        "WHERE (hit.timestamp BETWEEN :start AND :end) AND (:#{#uris == null} = true or hit.uri in :uris) " +
        "GROUP BY hit.app, hit.uri " +
        "ORDER BY COUNT(DISTINCT hit.ip) DESC"
    )
    List<ViewStats> getViewStatsWithUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(
            "SELECT new ru.practicum.server.model.ViewStats(hit.app, hit.uri, count(hit.ip)) " +
                    "FROM EndpointHit hit " +
                    "WHERE (hit.timestamp BETWEEN :start AND :end) AND (:#{#uris == null} = true or hit.uri in :uris) " +
                    "GROUP BY hit.app, hit.uri " +
                    "ORDER BY COUNT(hit.ip) DESC"
    )
    List<ViewStats> getViewStatsWithAllIp(LocalDateTime start, LocalDateTime end, List<String> uris);
}
