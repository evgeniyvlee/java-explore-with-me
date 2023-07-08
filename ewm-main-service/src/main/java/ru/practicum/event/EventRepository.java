package ru.practicum.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    Sort SORT_EVENT_DATE_DESC = Sort.by(Sort.Direction.DESC, "eventDate");

    List<Event> findByInitiatorId(Long userId, Pageable pageable);

    List<Event> findByIdIn(List<Long> eventIds);

    Optional<Event> findByIdAndState(Long eventId, EventState state);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long initiatorId);

    Optional<Event> findByCategoryId(Long categoryId);

    @Query("SELECT event " +
            "FROM Event event " +
            "WHERE (:#{#initiatorIds == null} = true or event.initiator.id in :initiatorIds) AND " +
            "(:#{#states == null} = true or event.state in :states) AND " +
            "(:#{#categories == null} = true or event.category.id in :categories) AND " +
            "(event.eventDate BETWEEN COALESCE(:rangeStart, event.eventDate) " +
            "AND COALESCE(:rangeEnd, event.eventDate))")
    List<Event> search(List<Long> initiatorIds, List<EventState> states, List<Long> categories,
                       LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query("SELECT event FROM Event event " +
            "WHERE ( " +
            "   (:text IS NULL OR LOWER(event.annotation) LIKE LOWER(CONCAT('%', :text, '%')))" +
            "       OR (:text IS NULL OR LOWER(event.description) LIKE LOWER(CONCAT('%', :text, '%'))) " +
            "           OR (:text IS NULL OR LOWER(event.title) LIKE LOWER(CONCAT('%', :text, '%')))" +
            ") AND (:#{#categories == null} = true or event.category.id in :categories) " +
            "   AND (:paid IS NULL OR event.paid = :paid) " +
            "       AND (event.eventDate BETWEEN COALESCE(:rangeStart, event.eventDate) " +
            "               AND COALESCE(:rangeEnd, event.eventDate))")
    List<Event> search(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                       LocalDateTime rangeEnd, Pageable pageable);
}
