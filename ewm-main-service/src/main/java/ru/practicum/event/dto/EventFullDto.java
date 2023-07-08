package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.Location;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.util.EwmServiceConstants;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {

    private Long id;

    private String title;

    private String annotation;

    private CategoryDto category;

    private Boolean paid;

    @JsonFormat(pattern = EwmServiceConstants.DATE_TIME_PATTERN)
    private LocalDateTime eventDate;

    private UserShortDto initiator;

    private String description;

    private Integer participantLimit;

    private EventState state;

    @JsonFormat(pattern = EwmServiceConstants.DATE_TIME_PATTERN)
    private LocalDateTime createdOn;

    @JsonFormat(pattern = EwmServiceConstants.DATE_TIME_PATTERN)
    private LocalDateTime publishedOn;

    private Location location;

    private Boolean requestModeration;

    private Long confirmedRequests = 0L;

    private Long views = 0L;
}
