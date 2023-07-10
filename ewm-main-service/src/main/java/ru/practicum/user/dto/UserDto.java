package ru.practicum.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.exception.ExceptionMessages;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank
    @Size(min = 2, max = 250, message = ExceptionMessages.USER_NAME_LENGTH_INVALID)
    private String name;

    @NotBlank
    @Email
    @Size(min = 6, max = 254, message = ExceptionMessages.USER_EMAIL_LENGTH_INVALID)
    private String email;
}
