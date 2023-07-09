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

    @Size(min = 20, max = 2000,  message = ExceptionMessages.EVENT_ANNOTATION_LENGTH_INVALID)
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000, message = ExceptionMessages.EVENT_DESCRIPTION_LENGTH_INVALID)
    private String description;

    @JsonFormat(pattern = EwmServiceConstants.DATE_TIME_PATTERN)
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private EventStateAction stateAction;

    @Size(min = 3, max = 120, message = ExceptionMessages.EVENT_TITLE_LENGTH_INVALID)
    private String title;
}
