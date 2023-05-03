package ru.practicum.ewm.privateApi.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.base.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.privateApi.service.request.PrivateRequestService;


@RestController
@Slf4j
@RequestMapping("/users/{userId}/requests")
public class PrivateRequestController {

    private final PrivateRequestService privateRequestService;

    @Autowired
    public PrivateRequestController(PrivateRequestService privateRequestService) {
        this.privateRequestService = privateRequestService;
    }

    @GetMapping
    public ResponseEntity<Object> getRequests(@PathVariable Long userId) {
        log.info("Получен запрос к эндпоинту: {} /users/{}/requests", "GET", userId);
        return new ResponseEntity<>(
                privateRequestService.getRequests(userId),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> create(@PathVariable Long userId,
                                                          @RequestParam Long eventId) {
        log.info("Получен запрос к эндпоинту: {} /users/{}/requests; eventId = {}", "POST", userId, eventId);
        return new ResponseEntity<>(
                privateRequestService.create(userId, eventId),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/{requestsId}/cancel")
    public ResponseEntity<ParticipationRequestDto> update(@PathVariable Long userId, @PathVariable Long requestsId) {
        log.info("Получен запрос к эндпоинту: {} /users/{}/requests/{}/cancel", "PATCH", userId, requestsId);
        return new ResponseEntity<>(
                privateRequestService.update(userId, requestsId),
                HttpStatus.OK
        );
    }


}
