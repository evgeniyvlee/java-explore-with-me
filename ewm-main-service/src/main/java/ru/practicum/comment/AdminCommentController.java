package ru.practicum.comment;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.util.LoggingMessages;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/admin/comments")
@Validated
public class AdminCommentController {

    private final CommentService commentService;

    @GetMapping("/users")
    public List<CommentDto> getAllByUserIds(@RequestParam(value = "users", required = false) List<Long> users,
                                            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                            @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.debug(LoggingMessages.GET_ALL.toString());
        return commentService.getAllByUserIds(users, from, size);
    }

    @GetMapping("/events")
    public List<CommentDto> getAllByEventIds(@RequestParam(value = "events", required = false) List<Long> events,
                                            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                            @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.debug(LoggingMessages.GET_ALL.toString());
        return commentService.getAllByEventIds(events, from, size);
    }
}
