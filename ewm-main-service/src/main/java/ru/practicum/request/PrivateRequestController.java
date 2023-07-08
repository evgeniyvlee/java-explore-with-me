package ru.practicum.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.util.LoggingMessages;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/users/{userId}/requests")
@Validated
public class PrivateRequestController {

    private final RequestService requestService;

    @GetMapping
    public List<ParticipationRequestDto> getByUserId(@PathVariable Long userId) {
        log.debug(LoggingMessages.GET.toString(), userId);
        return requestService.getByRequesterId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto create(@PathVariable Long userId, @RequestParam Long eventId) {
        log.debug(LoggingMessages.CREATE.toString(), eventId);
        return  requestService.create(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable Long userId, @PathVariable Long requestId) {
        log.debug(LoggingMessages.UPDATE.toString(), requestId);
        return requestService.cancel(userId, requestId);
    }
}
