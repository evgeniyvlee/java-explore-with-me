package ru.practicum.stat;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.util.EwmServiceConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsClient client;

    private final ObjectMapper objectMapper;

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern(EwmServiceConstants.DATE_TIME_PATTERN);

    private final String path = "/stats?start={start}&end={end}&uri={uri}&unique={unique}";

    @Override
    public void createHit(HttpServletRequest request) {
        EndpointHitDto endpointHitDto = new EndpointHitDto();
        endpointHitDto.setApp("ewm-main-service");
        endpointHitDto.setIp(request.getRemoteAddr());
        endpointHitDto.setTimestamp(LocalDateTime.now());
        endpointHitDto.setUri(request.getRequestURI());
        client.post("/hit", endpointHitDto);
    }

    @Override
    public Map<Long, Long> getViews(LocalDateTime start, LocalDateTime end, List<Long> eventIds, Boolean unique) {
        Map<Long, Long> result = new HashMap<>();

        Map<String, Long> uris = new HashMap<>();
        for (Long id : eventIds) {
            uris.put("/events/" + id, id);
        }
        Map<String, Object> parameters = Map.of(
                "start", start.format(formatter),
                "end", end.format(formatter),
                "uri", uris.keySet(),
                "unique", unique
        );

        ResponseEntity<Object> responseEntity = client.get(path, parameters);
        List<ViewStatsDto> statsDtoList = toViewStatsDtoList(responseEntity);
        System.out.println("statsDtoList" + statsDtoList);

        for (ViewStatsDto statsDto : statsDtoList) {
            Long eventId = uris.get(statsDto.getUri());
            if (eventId != null) {
                result.put(eventId, statsDto.getHits());
            }
        }
        return result;
    }

    private List<ViewStatsDto> toViewStatsDtoList(ResponseEntity<Object> responseEntity) {
        List<ViewStatsDto> statsDtoList;
        try {
            statsDtoList = Arrays.asList(
                    objectMapper.readValue(
                            objectMapper.writeValueAsString(responseEntity.getBody()), ViewStatsDto[].class)
            );
        } catch (JsonProcessingException e) {
            throw new ClassCastException(e.getMessage());
        }
        return statsDtoList;
    }
}
