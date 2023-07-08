package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.CategoryRepository;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventSort;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.EventStateAction;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.exception.ExceptionMessages;
import ru.practicum.exception.ValidationException;
import ru.practicum.request.RequestRepository;
import ru.practicum.request.model.Pair;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.stat.StatsService;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;
import ru.practicum.util.PageSettings;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    private final EventRepository eventRepository;

    private final RequestRepository requestRepository;

    private final StatsService statsService;

    @Override
    @Transactional
    public EventFullDto create(Long userId, NewEventDto newEventDto) {
        User user = getUserById(userId);
        Category category = getCategoryById(newEventDto.getCategory());
        Event event = EventMapper.toEvent(newEventDto, user, category);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getByUserId(Long userId, Integer from, Integer size, HttpServletRequest request) {
        Pageable pageable = new PageSettings(from, size, EventRepository.SORT_EVENT_DATE_DESC);
        List<EventShortDto> eventShortDtoList = eventRepository.findByInitiatorId(userId, pageable).stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
        statsService.createHit(request);
        return eventShortDtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getByEventId(Long eventId, HttpServletRequest request) {
        Event event = getEventByIdAndState(eventId, EventState.PUBLISHED);
        Long views = getViews(event);
        Long confirmedRequests = requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
        statsService.createHit(request);
        return EventMapper.toEventFullDto(event, confirmedRequests, views);
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getByUserIdAndEventId(Long eventId, Long initiatorId, HttpServletRequest request) {
        Event event = getEventByIdAndInitiatorId(eventId, initiatorId);
        Long views = getViews(event);
        Long confirmedRequests = requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
        statsService.createHit(request);
        return EventMapper.toEventFullDto(event, confirmedRequests, views);
    }

    @Override
    @Transactional
    public EventFullDto update(Long userId, Long eventId, UpdateEventRequest updatedEvent) {
        User user = getUserById(userId);
        Event event = getEventById(eventId);
        validate(user, event, updatedEvent);
        updateEventFields(event, updatedEvent);
        return EventMapper.toEventFullDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> search(List<Long> initiatorIds, List<EventState> states, List<Long> categories,
                                      LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        Pageable pageable = new PageSettings(from, size, EventRepository.SORT_EVENT_DATE_DESC);
        List<Event> events = eventRepository.search(initiatorIds, states, categories, rangeStart, rangeEnd, pageable);
        Map<Long, Long> confirmedRequests = getConfirmedRequests(events);
        Map<Long, Long> views = getViews(events);
        List<EventFullDto> eventShortDtoList = events.stream()
                .map(event -> {
                    Long eventId = event.getId();
                    return EventMapper.toEventFullDto(event, confirmedRequests.get(eventId), views.get(eventId));
                }).collect(Collectors.toList());
        return eventShortDtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> search(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                      LocalDateTime rangeEnd, Boolean onlyAvailable, EventSort sort, Integer from,
                                      Integer size, HttpServletRequest request) {
        Pageable pageable = new PageSettings(from, size, EventRepository.SORT_EVENT_DATE_DESC);
        rangeStart = (rangeStart != null ? rangeStart : LocalDateTime.now());
        validateDateRange(rangeStart, rangeEnd);

        List<Event> events = eventRepository.search(text, categories, paid, rangeStart, rangeEnd, pageable);
        Map<Long, Long> confirmedRequests = getConfirmedRequests(events);
        Map<Long, Long> views = getViews(events);

        List<EventShortDto> eventShortDtoList = events.stream()
                .map(event -> {
                    Long eventId = event.getId();
                    return EventMapper.toEventShortDto(event, confirmedRequests.get(eventId), views.get(eventId));
                }).collect(Collectors.toList());

        if (sort == EventSort.VIEWS) {
            Collections.sort(eventShortDtoList, Comparator.comparing(EventShortDto::getViews));
        }
        statsService.createHit(request);
        return eventShortDtoList;
    }

    @Override
    @Transactional
    public EventFullDto update(Long eventId, UpdateEventRequest updatedEvent) {
        Event event = getEventById(eventId);
        validate(event, updatedEvent);
        updateEventFields(event, updatedEvent);
        return EventMapper.toEventFullDto(event);
    }

    private Long getViews(Event event) {
        if (event == null) return 0L;

        Long eventId = event.getId();
        Map<Long, Long> views = statsService.getViews(event.getPublishedOn(), LocalDateTime.now(),
                List.of(eventId), true);
        System.out.println("views = " + views);
        return views.get(eventId);
    }

    @Transactional(readOnly = true)
    private Map<Long, Long> getConfirmedRequests(List<Event> events) {
        if ((events == null) || (events.isEmpty())) return new HashMap<>();

        List<Pair> confirmedRequestsList = requestRepository.findByEventIdInAndStatus(
                events.stream().map(Event::getId).collect(Collectors.toList()),
                RequestStatus.CONFIRMED);

        return confirmedRequestsList.stream()
                .collect(Collectors.toMap(Pair::getId, Pair::getHit));
    }

    private Map<Long, Long> getViews(List<Event> events) {
        if ((events == null) || (events.isEmpty())) return new HashMap<>();

        List<Event> sortedEvents = events.stream()
                .sorted(Comparator.comparing(Event::getPublishedOn)).collect(Collectors.toList());
        LocalDateTime start = sortedEvents.get(0).getPublishedOn();

        Map<Long, Long> views = statsService.getViews(start, LocalDateTime.now(),
                events.stream().map(Event::getId).collect(Collectors.toList()), true);
        return views;
    }

    @Transactional(readOnly = true)
    private Event getEventByIdAndInitiatorId(Long eventId, Long initiatorId) {
        return eventRepository.findByIdAndInitiatorId(eventId, initiatorId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.EVENT_NOT_FOUND));
    }

    @Transactional
    private void updateEventFields(Event event, UpdateEventRequest updatedEvent) {
        LocalDateTime eventDate = updatedEvent.getEventDate();
        if (eventDate != null) {
            event.setEventDate(eventDate);
        }

        EventStateAction stateAction = updatedEvent.getStateAction();
        if (stateAction != null) {
            switch (stateAction) {
                case SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
                    break;
                case CANCEL_REVIEW:
                case REJECT_EVENT:
                    event.setState(EventState.CANCELED);
                    break;
                case PUBLISH_EVENT:
                    if (event.getState() == EventState.PENDING) {
                        event.setState(EventState.PUBLISHED);
                        event.setPublishedOn(LocalDateTime.now());
                    }
                    break;
                default:
                    throw new IllegalArgumentException(ExceptionMessages.UNKNOWN_STATE_ACTION);
            }
        }

        if (updatedEvent.getAnnotation() != null) {
            event.setAnnotation(updatedEvent.getAnnotation());
        }
        if (updatedEvent.getCategory() != null) {
            Category eventCat = getCategoryById(updatedEvent.getCategory());
            event.setCategory(eventCat);
        }

        if (updatedEvent.getDescription() != null) {
            event.setDescription(updatedEvent.getDescription());
        }

        if (updatedEvent.getLocation() != null) {
            event.setLocation(updatedEvent.getLocation());
        }

        if (updatedEvent.getPaid() != null) {
            event.setPaid(updatedEvent.getPaid());
        }

        if (updatedEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updatedEvent.getParticipantLimit());
        }

        if (updatedEvent.getRequestModeration() != null) {
            event.setRequestModeration(updatedEvent.getRequestModeration());
        }

        if (updatedEvent.getTitle() != null) {
            event.setTitle(updatedEvent.getTitle());
        }
    }

    private void validate(User user, Event event, UpdateEventRequest updatedEvent) {
        long userId = user.getId();
        long initiatorId = event.getInitiator().getId();
        if (userId != initiatorId) {
            throw new ValidationException(ExceptionMessages.USER_IS_NOT_EVENT_INITIATOR);
        }

        if (event.getState() == EventState.PUBLISHED) {
            throw new ValidationException(ExceptionMessages.EVENT_NOT_PENDING_OR_CANCELED);
        }

        LocalDateTime eventDate = updatedEvent.getEventDate();
        if ((eventDate != null) && (eventDate.isBefore(LocalDateTime.now().plusHours(2L)))) {
            throw new BadRequestException(ExceptionMessages.EVENT_DATE_EARLY_2_HOURS_FROM_NOW);
        }
    }

    private void validate(Event event, UpdateEventRequest updatedEvent) {
        LocalDateTime eventDate = updatedEvent.getEventDate();
        if ((eventDate != null) && (eventDate.isBefore(LocalDateTime.now().plusHours(2L)))) {
            throw new BadRequestException(ExceptionMessages.EVENT_DATE_EARLY_2_HOURS_FROM_NOW);
        }

        if ((updatedEvent.getStateAction() == EventStateAction.PUBLISH_EVENT)
                && (event.getState() != EventState.PENDING)) {
            throw new ValidationException("");
        }

        if ((updatedEvent.getStateAction() == EventStateAction.REJECT_EVENT)
                && (event.getState() == EventState.PUBLISHED)) {
            throw new ValidationException("");
        }
    }

    @Transactional(readOnly = true)
    private Event getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.EVENT_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    private Event getEventByIdAndState(Long eventId, EventState state) {
        return eventRepository.findByIdAndState(eventId, state)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.EVENT_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    private Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.CATEGORY_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.USER_NOT_FOUND));
    }

    private void validateDateRange(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException(ExceptionMessages.INVALID_EVENT_RANGE_DATE);
        }
    }
}
