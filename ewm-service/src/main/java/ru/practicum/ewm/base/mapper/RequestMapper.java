package ru.practicum.ewm.base.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.base.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.base.model.Event;
import ru.practicum.ewm.base.model.Request;
import ru.practicum.ewm.base.model.User;

import java.time.LocalDateTime;

import static ru.practicum.ewm.base.enums.Status.CONFIRMED;
import static ru.practicum.ewm.base.enums.Status.PENDING;

@Mapper(componentModel = "spring")
public abstract class RequestMapper {
    public Request toRequest(Event event, User requester) {
        return Request.builder()
                .requester(requester)
                .event(event)
                .created(LocalDateTime.now())
                .status(event.getRequestModeration() ? PENDING : CONFIRMED)
                .build();
    }

    @Mapping(target = "requester", source = "request.requester.id")
    @Mapping(target = "event", source = "request.event.id")
    public abstract ParticipationRequestDto toParticipationRequestDto(Request request);
}
