package ru.practicum.event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PatchMapping;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventRequest;
import ru.practicum.request.RequestService;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.util.LoggingMessages;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/users/{userId}/events")
@Validated
public class PrivateEventController {

    private final EventService eventService;

    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@PathVariable Long userId, @Valid @RequestBody NewEventDto newEventDto) {
        log.debug(LoggingMessages.CREATE.toString(), newEventDto);
        return eventService.create(userId, newEventDto);
    }

    @GetMapping
    public List<EventShortDto> getByUserId(@PathVariable Long userId,
                                           @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                           @RequestParam(name = "size", defaultValue = "10") @Positive Integer size,
                                           HttpServletRequest request) {
        log.debug(LoggingMessages.GET.toString());
        return eventService.getByUserId(userId, from, size, request);
    }

    @GetMapping("{eventId}")
    public EventFullDto getByUserIdAndEventId(@PathVariable Long userId, @PathVariable Long eventId,
                                              HttpServletRequest request) {
        log.debug(LoggingMessages.GET.toString(), eventId);
        return eventService.getByUserIdAndEventId(eventId, userId, request);
    }

    @PatchMapping("{eventId}")
    public EventFullDto update(@PathVariable Long userId, @PathVariable Long eventId,
                               @Valid @RequestBody UpdateEventRequest updatedEvent) {
        log.debug(LoggingMessages.UPDATE.toString(), eventId);
        return eventService.update(userId, eventId, updatedEvent);
    }

    @GetMapping("{eventId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        log.debug(LoggingMessages.GET.toString(), eventId);
        return requestService.getByUserIdAndEventId(userId, eventId);
    }

    @PatchMapping("{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequests(@PathVariable Long userId, @PathVariable Long eventId,
                                                         @RequestBody EventRequestStatusUpdateRequest updatedRequest) {
        log.debug(LoggingMessages.UPDATE.toString(), eventId);
        return requestService.updateRequests(userId, eventId, updatedRequest);
    }
}
