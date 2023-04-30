package ru.practicum.ewm.publicApi.service.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatClient;

import ru.practicum.dto.StatDto;
import ru.practicum.ewm.base.mapper.EventMapper;
import ru.practicum.ewm.base.repository.EventCriteriaRepository;
import ru.practicum.ewm.base.repository.EventRepository;
import ru.practicum.ewm.base.dto.event.EventFullDto;
import ru.practicum.ewm.base.dto.event.EventShortDto;
import ru.practicum.ewm.base.enums.State;
import ru.practicum.ewm.base.exception.ConflictException;
import ru.practicum.ewm.base.exception.NotFoundException;
import ru.practicum.ewm.base.model.Event;
import ru.practicum.ewm.base.model.EventSearchCriteria;
import ru.practicum.ewm.base.util.page.MyPageRequest;
import ru.practicum.ewm.publicApi.dto.RequestParamForEvent;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class PublicEventsServiceImpl implements PublicEventsService {

    private final EventRepository eventRepository;

    private final EventCriteriaRepository eventCriteriaRepository;

    private final StatClient statsClient;

    private final EventMapper eventMapper;

    @Autowired
    public PublicEventsServiceImpl(EventRepository eventRepository,
                                   EventCriteriaRepository eventCriteriaRepository,
                                   StatClient statsClient,
                                   EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventCriteriaRepository = eventCriteriaRepository;
        this.statsClient = statsClient;
        this.eventMapper = eventMapper;
    }

    @Transactional
    @Override
    public List<EventShortDto> getAll(RequestParamForEvent param) {
        MyPageRequest pageable = createPageable(param.getSort(), param.getFrom(), param.getSize());
        EventSearchCriteria eventSearchCriteria = createCriteria(param);

        List<EventShortDto> eventShorts = eventCriteriaRepository
                .findAllWithFilters(pageable, eventSearchCriteria).stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());

        log.info("Get events list size: {}", eventShorts.size());
        saveEndpointHit(param.getRequest());
        return eventShorts;
    }


    @Transactional
    @Override
    public EventFullDto get(Long id, HttpServletRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Event not found with id = %s", id)));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException(String.format("Event with id=%d is not published", id));
        }

        saveEndpointHit(request);
        log.info("Get event: {}", event.getId());
        event.setViews(event.getViews() + 1);
        eventRepository.flush();
        return eventMapper.toEventFullDto(event);
    }

    private void saveEndpointHit(HttpServletRequest request) {
        try {
            URI uri = new URI(request.getRequestURI());
            StatDto statDto = StatDto.builder()
                    .ip(request.getRemoteAddr())
                    .uri(uri)
                    .app("ewm-main-service")
                    .timestamp(LocalDateTime.now())
                    .build();
            statsClient.postStat(statDto);
        } catch (URISyntaxException e) {
            log.warn(e.getMessage());
        }
    }

    private MyPageRequest createPageable(String sort, int from, int size) {
        MyPageRequest pageable = null;
        if (sort == null || sort.equals("EVENT_DATE")) {
            pageable = new MyPageRequest(from, size,
                    Sort.by(Sort.Direction.ASC, "event_date"));
        } else if (sort.equals("VIEWS")) {
            pageable = new MyPageRequest(from, size,
                    Sort.by(Sort.Direction.ASC, "views"));
        }
        return pageable;
    }

    private EventSearchCriteria createCriteria(RequestParamForEvent param) {
        return EventSearchCriteria.builder()
                .text(param.getText())
                .categories(param.getCategories())
                .rangeEnd(param.getRangeEnd())
                .rangeStart(param.getRangeStart())
                .paid(param.getPaid())
                .build();
    }
}
