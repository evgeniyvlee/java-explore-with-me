package ru.practicum.comment;

import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.dto.UpdateCommentDto;
import java.util.List;

public interface CommentService {
    CommentDto create(Long userId, Long eventId, NewCommentDto newCommentDto);

    List<CommentDto> getAllByUserId(Long userId, Integer from, Integer size);

    CommentDto getAllByUserAndEventId(Long userId, Long eventId);

    void delete(Long userId, Long commentId);

    CommentDto update(Long userId, Long commentId, UpdateCommentDto updateCommentDto);

    List<CommentDto> getAllByUserIds(List<Long> users, Integer from, Integer size);

    List<CommentDto> getAllByEventIds(List<Long> events, Integer from, Integer size);
}
