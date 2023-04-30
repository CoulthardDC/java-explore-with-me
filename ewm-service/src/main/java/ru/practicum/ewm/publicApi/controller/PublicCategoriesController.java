package ru.practicum.ewm.publicApi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.publicApi.service.category.PublicCategoriesService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@Slf4j
@RequestMapping("/categories")
@Validated
public class PublicCategoriesController {

    private final PublicCategoriesService publicCategoriesService;

    @Autowired
    public PublicCategoriesController(PublicCategoriesService publicCategoriesService) {
        this.publicCategoriesService = publicCategoriesService;
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                    @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Получен запрос к эндпоинту: {} /categories?from={}&size={}", "GET", from, size);
        return new ResponseEntity<>(
                publicCategoriesService.getAll(from, size),
                HttpStatus.OK
        );
    }

    @GetMapping("/{catId}")
    public ResponseEntity<Object> get(@PathVariable Long catId) {
        log.info("Получен запрос к эндпоинту: {} /categories/{}", "GET", catId);
        return new ResponseEntity<>(
                publicCategoriesService.get(catId),
                HttpStatus.OK
        );
    }
}
