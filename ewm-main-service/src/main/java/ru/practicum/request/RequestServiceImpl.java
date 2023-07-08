package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.practicum.event.EventRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.exception.ExceptionMessages;
import ru.practicum.exception.ValidationException;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getByRequesterId(Long requesterId) {
        getUserById(requesterId);
        return requestRepository.findByRequesterId(requesterId).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto create(Long userId, Long eventId) {
        User user = getUserById(userId);
        Event event = getEventById(eventId);
        validate(user, event);
        Request request = createNewRequest(event, user);
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancel(Long userId, Long requestId) {
        Request request = getRequestById(requestId);
        long requesterId = request.getRequester().getId();

        if (userId != requesterId) {
            throw new ValidationException(ExceptionMessages.IS_NOT_REQUESTER);
        }

        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toParticipationRequestDto(request);
    }

    @Override
    public List<ParticipationRequestDto> getByUserIdAndEventId(Long userId, Long eventId) {
        return requestRepository.findByUserIdAndEventId(userId, eventId).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult updateRequests(Long userId, Long eventId,
                                                         EventRequestStatusUpdateRequest updatedRequest) {
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();

        getUserById(userId);
        Event event = getEventById(eventId);
        List<Request> requests = requestRepository.findByIdIn(updatedRequest.getRequestIds());

        if ((!event.isRequestModeration() || event.getParticipantLimit() == 0)
                && (updatedRequest.getStatus() == RequestStatus.CONFIRMED)) {
            result.setConfirmedRequests(requests.stream()
                    .map(RequestMapper::toParticipationRequestDto)
                    .collect(Collectors.toList())
            );
            result.setRejectedRequests(Collections.emptyList());
            return result;
        }

        final AtomicLong vacancyLeft = new AtomicLong(event.getParticipantLimit()
                - requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED));

        if (vacancyLeft.get() <= 0) {
            throw new ValidationException(ExceptionMessages.EVENT_REQUEST_LIMIT);
        }

        List<Request> confirmedRequests = new ArrayList<>();
        List<Request> rejectedRequests = new ArrayList<>();

        requests.forEach(request -> {
            if (!request.getStatus().equals(RequestStatus.PENDING)) {
                throw new ValidationException(ExceptionMessages.EVENT_NOT_PENDING_OR_CANCELED);
            }
            if (updatedRequest.getStatus().equals(RequestStatus.CONFIRMED) && vacancyLeft.get() > 0) {
                request.setStatus(RequestStatus.CONFIRMED);
                confirmedRequests.add(request);
                vacancyLeft.decrementAndGet();
            } else {
                request.setStatus(RequestStatus.REJECTED);
                rejectedRequests.add(request);
            }
        });

        List<Request> allRequests = Stream.of(confirmedRequests, rejectedRequests)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        if (!allRequests.isEmpty()) {
            requestRepository.saveAll(allRequests);
        }

        result.setConfirmedRequests(confirmedRequests.stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList()));

        result.setRejectedRequests(rejectedRequests.stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList()));

        return result;
    }

    @Transactional(readOnly = true)
    private Request getRequestById(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.REQUEST_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    private void validate(User user, Event event) {
        long requesterId = user.getId();
        long initiatorId = event.getInitiator().getId();
        long eventId = event.getId();
        long participantLimit = event.getParticipantLimit();

        if (!CollectionUtils.isEmpty(requestRepository.findByRequesterIdAndEventId(requesterId, eventId))) {
            throw new ValidationException(ExceptionMessages.REQUEST_ALREADY_EXISTS);
        }

        if (requesterId == initiatorId) {
            throw new ValidationException(ExceptionMessages.EVENT_INITIATOR_IS_SAME_REQUESTER);
        }

        if (event.getState() != EventState.PUBLISHED) {
            throw new ValidationException(ExceptionMessages.EVENT_NOT_PUBLISHED);
        }

        Long countByEventIdAndStatus = requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);

        if ((participantLimit != 0)
                && (countByEventIdAndStatus >= participantLimit)) {
            throw new ValidationException(ExceptionMessages.EVENT_REQUEST_LIMIT);
        }
    }

    @Transactional(readOnly = true)
    private Event getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.EVENT_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.USER_NOT_FOUND));
    }

    Request createNewRequest(Event event, User user) {
        Request request = new Request();
        request.setCreated(LocalDateTime.now());
        request.setEvent(event);
        request.setRequester(user);
        request.setStatus((!event.isRequestModeration() || event.getParticipantLimit() == 0) ?
                RequestStatus.CONFIRMED : RequestStatus.PENDING
        );
        return request;
    }
}
