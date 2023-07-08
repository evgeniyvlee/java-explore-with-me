package ru.practicum.request;

import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> getByRequesterId(Long userId);

    ParticipationRequestDto create(Long userId, Long eventId);

    ParticipationRequestDto cancel(Long userId, Long requestId);

    List<ParticipationRequestDto> getByUserIdAndEventId(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateRequests(Long userId, Long eventId,
                                                  EventRequestStatusUpdateRequest updatedRequest);
}
