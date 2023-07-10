package ru.practicum.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.request.model.Pair;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;
import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByRequesterId(Long userId);

    List<Request> findByRequesterIdAndEventId(Long requesterId, Long eventId);

    @Query(
            "SELECT request " +
            "FROM Request request " +
            "WHERE (request.event.id = :eventId) AND (request.event.initiator.id = :userId) " +
            "ORDER BY request.id"
    )
    List<Request> findByUserIdAndEventId(Long userId, Long eventId);

    Long countByEventIdAndStatus(Long eventId, RequestStatus status);

    @Query(
            "SELECT new ru.practicum.request.model.Pair(request.event.id, COUNT(request.id)) " +
            "FROM Request request " +
            "WHERE (:#{#eventIds == null} = true or request.event.id in :eventIds) " +
            "   AND (request.status = :status) " +
            "GROUP BY request.event.id"
    )
    List<Pair> findByEventIdInAndStatus(List<Long> eventIds, RequestStatus status);

    List<Request> findByIdIn(List<Long> requestIds);


}
