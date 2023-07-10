package ru.practicum.comment;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.dto.UpdateCommentDto;
import ru.practicum.util.LoggingMessages;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/users/{userId}/comments")
@Validated
public class PrivateCommentController {

    private final CommentService commentService;

    @PostMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto create(@PathVariable Long userId, @PathVariable Long eventId,
                             @Valid @RequestBody NewCommentDto newCommentDto) {
        log.debug(LoggingMessages.CREATE.toString(), newCommentDto);
        return commentService.create(userId, eventId, newCommentDto);
    }

    @GetMapping
    public List<CommentDto> getAlByUserId(@PathVariable Long userId,
                                          @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                          @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.debug(LoggingMessages.GET_ALL.toString());
        return commentService.getAllByUserId(userId, from, size);
    }

    @GetMapping("/events/{eventId}")
    public CommentDto getAllByUserAndEventId(@PathVariable Long userId, @PathVariable Long eventId) {
        log.debug(LoggingMessages.GET_ALL.toString());
        return commentService.getAllByUserAndEventId(userId, eventId);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId, @PathVariable Long commentId) {
        log.debug(LoggingMessages.DELETE.toString(), commentId);
        commentService.delete(userId, commentId);
    }

    @PatchMapping("/{commentId}")
    public CommentDto update(@PathVariable Long userId, @PathVariable Long commentId,
                             @Valid @RequestBody UpdateCommentDto updateCommentDto) {
        log.debug(LoggingMessages.UPDATE.toString(), commentId);
        return commentService.update(userId, commentId, updateCommentDto);
    }
}
