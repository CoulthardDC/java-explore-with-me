package ru.practicum.ewm.privateApi.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.base.dto.event.*;
import ru.practicum.ewm.base.enums.Status;
import ru.practicum.ewm.base.exception.ConflictException;
import ru.practicum.ewm.privateApi.service.event.PrivateEventsService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@Slf4j
@RequestMapping("/users/{userId}/events")
@Validated
public class PrivateEventsController {

    private final PrivateEventsService privateEventsService;


    public PrivateEventsController(PrivateEventsService privateEventsService) {
        this.privateEventsService = privateEventsService;
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@PathVariable Long userId,
                                                      @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                      @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос к эндпоинту: {} /users/{}/events", "GET", userId);
        return new ResponseEntity<>(
                privateEventsService.getAll(userId, from, size),
                HttpStatus.OK
        );
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Object> get(@PathVariable Long userId,
                                            @PathVariable Long eventId) {
        log.info("Получен запрос к эндпоинту: {} /users/{}/events/{}", "GET", userId, eventId);
        return new ResponseEntity<>(
                privateEventsService.get(userId, eventId),
                HttpStatus.OK
        );
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<Object> getRequests(@PathVariable Long userId,
                                                                     @PathVariable Long eventId) {
        log.info("Получен запрос к эндпоинту: {} /users/{}/events/{}/requests", "GET", userId, eventId);
        return new ResponseEntity<>(
                privateEventsService.getRequests(userId, eventId),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<Object> create(@PathVariable Long userId,
                                               @RequestBody @Valid NewEventDto eventDto) {
        log.info("Получен запрос POST /users/{}/events c новым событием: {}", userId, eventDto);
        log.info("Получен запрос к эндпоинту: {} /users/{}/events", "POST", userId);
        return new ResponseEntity<>(
                privateEventsService.create(userId, eventDto),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<Object> update(@PathVariable Long userId, @PathVariable Long eventId,
                                               @RequestBody @Valid UpdateEventUserRequest eventDto) {
        log.info("Получен запрос к эндпоинту: {} /users/{}/events/{}", "PATCH", userId, eventId);
        return new ResponseEntity<>(
                privateEventsService.update(userId, eventId, eventDto),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> updateRequestStatus(@PathVariable Long userId,
                                                                              @PathVariable Long eventId,
                                                                              @RequestBody EventRequestStatusUpdateRequest request) {
        log.info("Получен запрос к эндпоинту: {} /users/{}/events/{}/requests", "PATCH", userId, eventId);
        if (Status.from(request.getStatus()) == null) {
            throw new ConflictException("Status is not validate");
        }
        return new ResponseEntity<>(
                privateEventsService.updateRequestStatus(userId, eventId, request),
                HttpStatus.OK
        );
    }

}
