package ru.practicum.server.model;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.EndpointHitDto;

@UtilityClass
public class EndpointHitMapper {

    public EndpointHit toEndpointHit(final EndpointHitDto endpointHitDto) {
        return new EndpointHit(endpointHitDto.getApp(), endpointHitDto.getUri(), endpointHitDto.getIp(),
                endpointHitDto.getTimestamp());
    }
}
