package ru.practicum.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.exception.ExceptionMessages;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCommentDto {
    // Comment text
    @NotBlank
    @Size(min = 1, max = 5000, message = ExceptionMessages.COMMENT_LENGTH_INVALID)
    private String text;
}