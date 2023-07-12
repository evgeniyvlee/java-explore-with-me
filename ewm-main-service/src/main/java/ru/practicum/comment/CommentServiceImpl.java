package ru.practicum.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.dto.UpdateCommentDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.event.EventRepository;
import ru.practicum.event.model.Event;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.exception.ExceptionMessages;
import ru.practicum.exception.ValidationException;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;
import ru.practicum.util.PageSettings;
import ru.practicum.util.SortSettings;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    private final CommentRepository commentRepository;

    @Override
    public CommentDto create(Long userId, Long eventId, NewCommentDto newCommentDto) {
        User user = getUserById(userId);
        Event event = getEventById(eventId);
        Comment comment = CommentMapper.toComment(newCommentDto, user, event);
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getAllByUserId(Long userId, Integer from, Integer size) {
        getUserById(userId);
        Pageable pageable = new PageSettings(from, size, SortSettings.SORT_ID_DESC);
        List<Comment> commentList = commentRepository.findByAuthorId(userId, pageable);
        return commentList.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CommentDto getAllByUserAndEventId(Long userId, Long eventId) {
        getUserById(userId);
        getEventById(eventId);
        return CommentMapper.toCommentDto(commentRepository.findByAuthorIdAndEventId(userId, eventId));
    }

    @Override
    public void delete(Long userId, Long commentId) {
        getUserById(userId);
        Comment comment = getCommentById(commentId);
        validateCommentAuthor(userId, comment);
        commentRepository.delete(comment);
    }

    @Override
    public CommentDto update(Long userId, Long commentId, UpdateCommentDto updateCommentDto) {
        getUserById(userId);
        Comment comment = getCommentById(commentId);
        validateCommentAuthor(userId, comment);
        comment.setText(updateCommentDto.getText());
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getAllByUserIds(List<Long> users, Integer from, Integer size) {
        Pageable pageable = new PageSettings(from, size, SortSettings.SORT_ID_DESC);
        List<Comment> commentList = commentRepository.findByAuthorIdIn(users, pageable);
        return commentList.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getAllByEventIds(List<Long> events, Integer from, Integer size) {
        Pageable pageable = new PageSettings(from, size, SortSettings.SORT_ID_DESC);
        List<Comment> commentList = commentRepository.findByEventIdIn(events, pageable);
        return commentList.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }

    private void validateCommentAuthor(Long userId, Comment comment) {
        if (!userId.equals(comment.getAuthor().getId())) {
            throw new ValidationException(ExceptionMessages.COMMENT_AUTHOR_WRONG);
        }
    }

    private Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.COMMENT_NOT_FOUND));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.USER_NOT_FOUND));
    }

    private Event getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.EVENT_NOT_FOUND));
    }
}
