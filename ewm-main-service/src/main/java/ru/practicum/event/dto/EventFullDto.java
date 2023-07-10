package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.Location;
import ru.practicum.util.EwmServiceConstants;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto extends EventShortDto {

    private String description;

    private Integer participantLimit;

    private EventState state;

    @JsonFormat(pattern = EwmServiceConstants.DATE_TIME_PATTERN)
    private LocalDateTime createdOn;

    @JsonFormat(pattern = EwmServiceConstants.DATE_TIME_PATTERN)
    private LocalDateTime publishedOn;

    private Location location;

    private Boolean requestModeration;
}
