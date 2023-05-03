package ru.practicum.ewm.privateApi.service.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.base.mapper.EventMapper;
import ru.practicum.ewm.base.mapper.RequestMapper;
import ru.practicum.ewm.base.repository.CategoriesRepository;
import ru.practicum.ewm.base.repository.EventRepository;
import ru.practicum.ewm.base.repository.RequestRepository;
import ru.practicum.ewm.base.repository.UserRepository;
import ru.practicum.ewm.base.dto.event.*;
import ru.practicum.ewm.base.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.base.enums.State;
import ru.practicum.ewm.base.enums.UserStateAction;
import ru.practicum.ewm.base.exception.ConflictException;
import ru.practicum.ewm.base.exception.NotFoundException;
import ru.practicum.ewm.base.model.Event;
import ru.practicum.ewm.base.model.Request;
import ru.practicum.ewm.base.util.UtilMergeProperty;
import ru.practicum.ewm.base.util.page.MyPageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.base.enums.Status.CONFIRMED;
import static ru.practicum.ewm.base.enums.Status.REJECTED;

@Service
@Slf4j
@Transactional(readOnly = true)
public class PrivateEventsServiceImpl implements PrivateEventsService {

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final RequestRepository requestRepository;

    private final CategoriesRepository categoriesRepository;

    private final EventMapper eventMapper;

    private final RequestMapper requestMapper;

    @Autowired
    public PrivateEventsServiceImpl(EventRepository eventRepository,
                                    UserRepository userRepository,
                                    RequestRepository requestRepository,
                                    CategoriesRepository categoriesRepository,
                                    EventMapper eventMapper,
                                    RequestMapper requestMapper) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.categoriesRepository = categoriesRepository;
        this.eventMapper = eventMapper;
        this.requestMapper = requestMapper;
    }

    @Override
    public List<EventShortDto> getAll(Long userId, Integer from, Integer size) {
        MyPageRequest pageRequest = new MyPageRequest(from, size,
                Sort.by(Sort.Direction.ASC, "id"));
        List<EventShortDto> eventShorts = eventRepository.findAll(pageRequest).toList().stream()
                        .map(eventMapper::toEventShortDto)
                        .collect(Collectors.toList());
        log.info("Get events list size: {}", eventShorts.size());
        return eventShorts;
    }

    @Override
    public EventFullDto get(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Event not found with id = %s and userId = %s", eventId, userId)));
        log.info("Get event: {}", event.getId());
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public List<ParticipationRequestDto> getRequests(Long userId, Long eventId) {
        if (!eventRepository.existsByIdAndInitiatorId(eventId, userId)) {
            throw new NotFoundException(
                    String.format("Event not found with id = %s and userId = %s", eventId, userId));
        }
        return requestRepository.findAllByEventId(eventId).stream()
                .map(requestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto create(Long userId, NewEventDto eventDto) {
        checkEventDate(eventDto.getEventDate());
        Event event = eventMapper.toEvent(eventDto);
        event.setCategory(categoriesRepository.findById(eventDto.getCategory())
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found",
                        eventDto.getCategory()))));
        event.setConfirmedRequests(0L);
        event.setPublishedOn(LocalDateTime.now());
        event.setInitiator(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found", userId))));
        event.setViews(0L);
        try {
            event = eventRepository.save(event);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage(), e);
        }
        log.info("Add event: {}", event.getTitle());
        return eventMapper.toEventFullDto(event);
    }

    @Transactional
    @Override
    public EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest eventDto) {
        Event eventTarget = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Event not found with id = %s and userId = %s", eventId, userId)));
        Event eventUpdate = eventMapper.toEvent(eventDto);
        checkEventDate(eventUpdate.getDate());

        if (eventDto.getCategory() != null) {
            eventUpdate.setCategory(categoriesRepository.findById(eventDto.getCategory())
                    .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found",
                            eventDto.getCategory()))));
        }

        if (eventTarget.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Event must not be published");
        }

        UtilMergeProperty.copyProperties(eventUpdate, eventTarget);
        if (UserStateAction.CANCEL_REVIEW.toString().equals(eventDto.getStateAction())) {
            eventTarget.setState(State.CANCELED);
        } else if (UserStateAction.SEND_TO_REVIEW.toString().equals(eventDto.getStateAction())) {
            eventTarget.setState(State.PENDING);
        }

        eventRepository.flush();
        log.info("Update event: {}", eventTarget.getTitle());
        return eventMapper.toEventFullDto(eventTarget);
    }


    @Transactional
    @Override
    public EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest request) {
        List<ParticipationRequestDto> confirmedRequests = List.of();
        List<ParticipationRequestDto> rejectedRequests = List.of();

        List<Long> requestIds = request.getRequestIds();
        List<Request> requests = requestRepository.findAllByIdIn(requestIds);

        String status = request.getStatus();

        if (status.equals(REJECTED.toString())) {
            if (status.equals(REJECTED.toString())) {
                boolean isConfirmedRequestExists = requests.stream()
                        .anyMatch(r -> r.getStatus().equals(CONFIRMED));
                if (isConfirmedRequestExists) {
                    throw new ConflictException("Cannot reject confirmed requests");
                }
                rejectedRequests = requests.stream()
                        .peek(r -> r.setStatus(REJECTED))
                        .map(requestMapper::toParticipationRequestDto)
                        .collect(Collectors.toList());
                return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
            }
        }

        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Event not found with id = %s and userId = %s", eventId, userId)));
        Long participantLimit = event.getParticipantLimit();
        Long approvedRequests = event.getConfirmedRequests();
        Long availableParticipants = participantLimit - approvedRequests;
        Long potentialParticipants = (long) requestIds.size();

        if (participantLimit > 0 && participantLimit.equals(approvedRequests)) {
            throw new ConflictException(String.format("Event with id=%d has reached participant limit", eventId));
        }

        if (status.equals(CONFIRMED.toString())) {
            if (participantLimit.equals(0L) || (potentialParticipants <= availableParticipants && !event.getRequestModeration())) {
                confirmedRequests = requests.stream()
                        .peek(r -> {
                            if (!r.getStatus().equals(CONFIRMED)) {
                                r.setStatus(CONFIRMED);
                            } else {
                                throw new ConflictException(String.format("Request with id=%d has already been confirmed", r.getId()));
                            }
                        })
                        .map(requestMapper::toParticipationRequestDto)
                        .collect(Collectors.toList());
                event.setConfirmedRequests(approvedRequests + potentialParticipants);
            } else {
                confirmedRequests = requests.stream()
                        .limit(availableParticipants)
                        .peek(r -> {
                            if (!r.getStatus().equals(CONFIRMED)) {
                                r.setStatus(CONFIRMED);
                            } else {
                                throw new ConflictException(String.format("Request with id=%d has already been confirmed", r.getId()));
                            }
                        })
                        .map(requestMapper::toParticipationRequestDto)
                        .collect(Collectors.toList());
                rejectedRequests = requests.stream()
                        .skip(availableParticipants)
                        .peek(r -> {
                            if (!r.getStatus().equals(REJECTED)) {
                                r.setStatus(REJECTED);
                            } else {
                                throw new ConflictException(String.format("Request with id=%d has already been rejected", r.getId()));
                            }
                        })
                        .map(requestMapper::toParticipationRequestDto)
                        .collect(Collectors.toList());
                event.setConfirmedRequests(participantLimit);
            }
        }
        eventRepository.flush();
        requestRepository.flush();
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }


    private void checkEventDate(LocalDateTime eventDate) {
        if (eventDate != null && eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("Field: eventDate. Error: the date and time for which the event is scheduled" +
                    " cannot be earlier than two hours from the current moment. Value: " + eventDate);
        }
    }

}
