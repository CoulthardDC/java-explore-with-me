package ru.practicum.ewm.publicApi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.publicApi.service.compilation.PublicCompilationsService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@Slf4j
@Validated
@RequestMapping("/compilations")
public class PublicCompilationsController {

    private final PublicCompilationsService publicCompilationsService;

    @Autowired
    public PublicCompilationsController(PublicCompilationsService publicCompilationsService) {
        this.publicCompilationsService = publicCompilationsService;
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestParam(defaultValue = "false") Boolean pinned,
                                                       @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                       @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Получен запрос к эндпоинту: {} /compilations?pinned={}&from={}&size={}", "GET", pinned, from, size);
        return new ResponseEntity<>(
                publicCompilationsService.getAll(pinned, from, size),
                HttpStatus.OK
        );
    }

    @GetMapping("/{comId}")
    public ResponseEntity<Object> get(@PathVariable Long comId) {
        log.info("Получен запрос к эндпоинту: {} /compilations/{}", "GET", comId);
        return new ResponseEntity<>(
                publicCompilationsService.get(comId),
                HttpStatus.OK
        );
    }
}
