package ru.practicum.event;

import ru.practicum.event.dto.*;
import ru.practicum.event.model.EventSort;
import ru.practicum.event.model.EventState;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventFullDto create(Long userId, NewEventDto newEventDto);

    List<EventShortDto> getByUserId(Long userId, Integer from, Integer size, HttpServletRequest request);

    EventFullDto getByUserIdAndEventId(Long eventId, Long initiatorId, HttpServletRequest request);

    EventFullDto update(Long userId, Long eventId, UpdateEventRequest updatedEvent);

    EventFullDto update(Long eventId, UpdateEventRequest updatedEvent);

    List<EventFullDto> search(List<Long> initiatorIds, List<EventState> states, List<Long> categories,
                               LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    List<EventShortDto> search(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                               LocalDateTime rangeEnd, Boolean onlyAvailable, EventSort sort, Integer from,
                               Integer size);

    EventFullDto getByEventId(Long eventId, HttpServletRequest request);
}
