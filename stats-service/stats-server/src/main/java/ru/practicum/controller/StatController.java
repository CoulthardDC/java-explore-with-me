package ru.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import ru.practicum.dto.StatDto;

import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.StatService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collection;

@RestController
@Slf4j
public class StatController {
    private final StatService statService;

    public StatController(StatService statService) {
        this.statService = statService;
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> postStats(@NonNull @RequestBody StatDto statDto) {
        log.info("Получен запрос к эндпоинту: {} /hit", "POST");
        System.out.println(statDto);
        return new ResponseEntity<>(
                statService.create(statDto),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                 LocalDateTime start,
                                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                 LocalDateTime end,
                                             @RequestParam(required = false) Collection<URI> uris,
                                             @RequestParam(required = false, defaultValue = "false") Boolean unique) {

        log.info("Получен запрос к эндпоинту: {} /stats?start={}&end={}&uris={}&unique={}", "GET", start, end, uris, unique);
        return new ResponseEntity<>(
                statService.getStats(start, end, uris, unique),
                HttpStatus.OK
        );
    }
}
