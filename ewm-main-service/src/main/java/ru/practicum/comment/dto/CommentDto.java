package ru.practicum.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.exception.ExceptionMessages;
import ru.practicum.util.EwmServiceConstants;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    // Comment ID
    private Long id;
    // Comment text
    @NotBlank
    @Size(min = 1, max = 5000, message = ExceptionMessages.COMMENT_LENGTH_INVALID)
    private String text;
    // Author name
    private String authorName;
    // Created date
    @JsonFormat(pattern = EwmServiceConstants.DATE_TIME_PATTERN)
    private LocalDateTime created;
}
