package ru.practicum.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class EndpointHitDto {
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    @NotBlank
    private String app;
    @NotBlank
    private String uri;
    @NotBlank
    private String ip;
    @NotNull
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime timestamp;
}
