package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.model.Location;
import ru.practicum.exception.ExceptionMessages;
import ru.practicum.util.EwmServiceConstants;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotBlank
    @Size(min = 20, message = ExceptionMessages.EVENT_ANNOTATION_TOO_SHORT)
    @Size(max = 2000, message = ExceptionMessages.EVENT_ANNOTATION_TOO_LONG)
    private String annotation;

    @NotNull
    private Long category;

    @NotBlank
    @Size(min = 20, message = ExceptionMessages.EVENT_DESCRIPTION_TOO_SHORT)
    @Size(max = 7000, message = ExceptionMessages.EVENT_DESCRIPTION_TOO_LONG)
    private String description;

    @NotNull
    @Future
    @JsonFormat(pattern = EwmServiceConstants.DATE_TIME_PATTERN)
    private LocalDateTime eventDate;

    @NotNull
    private Location location;

    private Boolean paid = Boolean.FALSE;

    private Integer participantLimit = 0;

    private Boolean requestModeration = Boolean.TRUE;

    @NotBlank
    @Size(min = 3, message = ExceptionMessages.EVENT_TITLE_TOO_SHORT)
    @Size(max = 120, message = ExceptionMessages.EVENT_TITLE_TOO_LONG)
    private String title;
}
