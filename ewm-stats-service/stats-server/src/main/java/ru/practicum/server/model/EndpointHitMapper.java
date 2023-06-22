package ru.practicum.server.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.dto.EndpointHitDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EndpointHitMapper {

    public static EndpointHit toEndpointHit(final EndpointHitDto endpointHitDto) {
        return new EndpointHit(endpointHitDto.getApp(), endpointHitDto.getUri(), endpointHitDto.getIp(),
                endpointHitDto.getTimestamp());
    }
}
