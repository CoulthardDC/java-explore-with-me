package ru.practicum.ewm.base.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.base.dto.event.*;
import ru.practicum.ewm.base.enums.State;
import ru.practicum.ewm.base.model.Event;
import ru.practicum.ewm.base.model.Location;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", uses = {LocationMapper.class, CategoryMapper.class, UserMapper.class, EventMapper.class})
public abstract class EventMapper {
    //@Mapping(target = "date", source = "eventDate")
    //@Mapping(ignore = true, target = "category")
    public Event toEvent(NewEventDto newEventDto) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .createdOn(LocalDateTime.now())
                .description(newEventDto.getDescription())
                .date(newEventDto.getEventDate())
                .location(new Location(newEventDto.getLocation().getLat(), newEventDto.getLocation().getLon()))
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .state(State.PENDING)
                .title(newEventDto.getTitle())
                .build();
    }

    public Event toEvent(UpdateEventAdminRequest updateEventAdminRequest) {
        return Event.builder()
                .annotation(updateEventAdminRequest.getAnnotation())
                .createdOn(LocalDateTime.now())
                .description(updateEventAdminRequest.getDescription())
                .date(updateEventAdminRequest.getEventDate())
                .location(updateEventAdminRequest.getLocation() != null ? new Location(updateEventAdminRequest.getLocation().getLat(),
                        updateEventAdminRequest.getLocation().getLon()) : null)
                .paid(updateEventAdminRequest.getPaid())
                .participantLimit(updateEventAdminRequest.getParticipantLimit())
                .requestModeration(updateEventAdminRequest.getRequestModeration())
                .state(State.from(updateEventAdminRequest.getStateAction()))
                .title(updateEventAdminRequest.getTitle())
                .build();
    }

    public Event toEvent(UpdateEventUserRequest updateEventUserRequest) {
        return Event.builder()
                .annotation(updateEventUserRequest.getAnnotation())
                .description(updateEventUserRequest.getDescription())
                .date(updateEventUserRequest.getEventDate())
                .paid(updateEventUserRequest.getPaid())
                .location(updateEventUserRequest.getLocation() != null ? new Location(updateEventUserRequest.getLocation().getLat(),
                        updateEventUserRequest.getLocation().getLon()) : null)
                .participantLimit(updateEventUserRequest.getParticipantLimit())
                .title(updateEventUserRequest.getTitle())
                .build();
    }

    @Mapping(target = "eventDate", source = "date")
    public abstract EventFullDto toEventFullDto(Event event);

    @Mapping(target = "eventDate", source = "date")
    public abstract EventShortDto toEventShortDto(Event event);

    @Mapping(target = "eventDate", source = "date")
    public abstract List<EventFullDto> toEventFullDtoList(List<Event> events);

    @Mapping(target = "eventDate", source = "date")
    public abstract List<EventShortDto> toEventShortDtoList(List<Event> events);
}
