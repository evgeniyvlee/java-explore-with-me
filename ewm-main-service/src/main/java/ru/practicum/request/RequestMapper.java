package ru.practicum.request;

import lombok.experimental.UtilityClass;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.Request;

@UtilityClass
public class RequestMapper {
    public ParticipationRequestDto toParticipationRequestDto(Request request) {
        ParticipationRequestDto participationRequestDto = new ParticipationRequestDto();
        if (request != null) {
            participationRequestDto.setId(request.getId());
            participationRequestDto.setCreated(request.getCreated());
            participationRequestDto.setEvent(request.getEvent().getId());
            participationRequestDto.setRequester(request.getRequester().getId());
            participationRequestDto.setStatus(request.getStatus());
        }
        return participationRequestDto;
    }
}
