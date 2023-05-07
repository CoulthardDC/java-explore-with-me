package ru.practicum.ewm.privateApi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.base.dto.comment.NewCommentDto;
import ru.practicum.ewm.privateApi.service.comment.PrivateCommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/users/{userId}/comments")
@Slf4j
@Validated
public class PrivateCommentController {

    private final PrivateCommentService privateCommentService;

    public PrivateCommentController(PrivateCommentService privateCommentService) {
        this.privateCommentService = privateCommentService;
    }

    @PostMapping
    public ResponseEntity<Object> createByPrivate(@PathVariable Long userId,
                                          @RequestParam Long eventId,
                                          @Valid @RequestBody NewCommentDto newCommentDto) {
        log.info("Получен запрос к эндпоинту:  {} /users/{}/comments", "POST", userId);
        return new ResponseEntity<>(
                privateCommentService.create(userId, eventId, newCommentDto),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> patchByPrivate(@PathVariable Long userId,
                                     @PathVariable Long commentId,
                                     @Valid @RequestBody NewCommentDto newCommentDto) {
        log.info("Получен запрос к эндпоинту: {} /users/{}/comments/{}", "PATCH", userId, commentId);
        return new ResponseEntity<>(
                privateCommentService.update(userId, commentId, newCommentDto),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByPrivate(@PathVariable Long userId,
                                @PathVariable Long commentId) {
        log.info("Получен запрос к эндпоинту: {} /users/{}/comments/{}", "DELETE", userId, commentId);
        privateCommentService.deleteComment(userId, commentId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getCommentsByPrivate(
            @PathVariable Long userId,
            @RequestParam(required = false) Long eventId,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.info("Поулчен запрос к эндпоинту: {} /users/{}/comments", "GET", userId);
        return new ResponseEntity<>(
                privateCommentService.getComments(userId, eventId, PageRequest.of(from / size, size)),
                HttpStatus.OK
        );
    }
}
