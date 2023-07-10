package ru.practicum.comment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.comment.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAuthorId(Long authorId, Pageable pageable);

    Comment findByAuthorIdAndEventId(Long authorId, Long eventId);

    @Query(
            "SELECT comment " +
            "FROM Comment comment " +
            "WHERE (:#{#users == null} = true or comment.author.id in :users)"
    )
    List<Comment> findByAuthorIdIn(List<Long> users, Pageable pageable);

    @Query(
            "SELECT comment " +
            "FROM Comment comment " +
            "WHERE (:#{#events == null} = true or comment.event.id in :events)"
    )
    List<Comment> findByEventIdIn(List<Long> events, Pageable pageable);
}
