package ru.practicum.ewm.adminApi.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.adminApi.dto.RequestParamForEvent;
import ru.practicum.ewm.adminApi.service.event.AdminEventsService;
import ru.practicum.ewm.base.dto.event.UpdateEventAdminRequest;
import ru.practicum.ewm.base.enums.State;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/admin/events")
@Validated
public class AdminEventsController {

    public final AdminEventsService adminEventsService;

    @Autowired
    public AdminEventsController(AdminEventsService adminEventsService) {
        this.adminEventsService = adminEventsService;
    }

    @GetMapping()
    public ResponseEntity<Object> getAll(@RequestParam(required = false) List<Long> users,
                                                     @RequestParam(required = false) List<String> states,
                                                     @RequestParam(required = false) List<Long> categories,
                                                     @RequestParam(required = false) String rangeStart,
                                                     @RequestParam(required = false) String rangeEnd,
                                                     @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                     @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Получен запрос к эндпоинту: {} /admin/events", "GET");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<State> statesEnum = null;
        if (states != null) {
            statesEnum = states.stream().map(State::from).filter(Objects::nonNull).collect(Collectors.toList());
        }

        RequestParamForEvent param = RequestParamForEvent.builder()
                .users(users)
                .states(statesEnum)
                .categories(categories)
                .rangeStart(rangeStart != null ? LocalDateTime.parse(rangeStart, dateTimeFormatter) : null)
                .rangeEnd(rangeEnd != null ? LocalDateTime.parse(rangeEnd, dateTimeFormatter) : null)
                .from(from)
                .size(size)
                .build();

        return new ResponseEntity<>(
                adminEventsService.getAll(param),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<Object> update(@PathVariable Long eventId,
                                               @RequestBody UpdateEventAdminRequest updateEvent) {
        log.info("Получен запрос к эндпоинту: {} /admin/events/{}", "PATCH", eventId);
        return new ResponseEntity<>(
                adminEventsService.update(eventId, updateEvent),
                HttpStatus.OK
        );
    }
}
