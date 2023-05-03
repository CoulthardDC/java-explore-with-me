package ru.practicum.ewm.publicApi.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.publicApi.dto.RequestParamForEvent;
import ru.practicum.ewm.publicApi.service.event.PublicEventsService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequestMapping("/events")
public class PublicEventsController {

    private final PublicEventsService publicEventsService;

    @Autowired
    public PublicEventsController(PublicEventsService publicEventsService) {
        this.publicEventsService = publicEventsService;
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestParam(required = false) String text,
                                                      @RequestParam(required = false) List<Long> categories,
                                                      @RequestParam(required = false) Boolean paid,
                                                      @RequestParam(required = false) String rangeStart,
                                                      @RequestParam(required = false) String rangeEnd,
                                                      @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                      @RequestParam(required = false) String sort,
                                                      @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                      @Positive @RequestParam(defaultValue = "10") int size,
                                                      HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: {} /events?text={}&categories={}&paid={}&rangeStart={}&rangeEnd={}" +
                        "&onlyAvailable={}&sort={}&from={}&size={}", "GET", text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size);
        RequestParamForEvent param = RequestParamForEvent.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .onlyAvailable(onlyAvailable)
                .sort(sort)
                .from(from)
                .size(size)
                .request(request)
                .build();

        return new ResponseEntity<>(
                publicEventsService.getAll(param),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable Long id, HttpServletRequest request) {
        log.info("Получен запрос GET /events/{}", id);
        log.info("Получен запрос к эндпоинту: {} /events/{}", "GET", id);
        return new ResponseEntity<>(
                publicEventsService.get(id, request),
                HttpStatus.OK
        );
    }
}
