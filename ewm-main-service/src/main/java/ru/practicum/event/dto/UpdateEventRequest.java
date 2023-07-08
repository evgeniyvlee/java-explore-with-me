package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.model.EventStateAction;
import ru.practicum.event.model.Location;
import ru.practicum.exception.ExceptionMessages;
import ru.practicum.util.EwmServiceConstants;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventRequest {

    @Size(min = 20, message = ExceptionMessages.EVENT_ANNOTATION_TOO_SHORT)
    @Size(max = 2000, message = ExceptionMessages.EVENT_ANNOTATION_TOO_LONG)
    private String annotation;

    private Long category;

    @Size(min = 20, message = ExceptionMessages.EVENT_DESCRIPTION_TOO_SHORT)
    @Size(max = 7000, message = ExceptionMessages.EVENT_DESCRIPTION_TOO_LONG)
    private String description;

    @JsonFormat(pattern = EwmServiceConstants.DATE_TIME_PATTERN)
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private EventStateAction stateAction;

    @Size(min = 3, message = ExceptionMessages.EVENT_TITLE_TOO_SHORT)
    @Size(max = 120, message = ExceptionMessages.EVENT_TITLE_TOO_LONG)
    private String title;
}
