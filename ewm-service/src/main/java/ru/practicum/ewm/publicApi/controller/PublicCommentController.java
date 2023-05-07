package ru.practicum.ewm.publicApi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.publicApi.service.comment.PublicCommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/comments")
@Slf4j
@Validated
public class PublicCommentController {

    private final PublicCommentService publicCommentService;

    @Autowired
    public PublicCommentController(PublicCommentService publicCommentService) {
        this.publicCommentService = publicCommentService;
    }

    @GetMapping
    public ResponseEntity<Object> getCommentsByPublic(
            @RequestParam Long eventId,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Получен запрос к энпоинту: {} /comments", "GET");
        return new ResponseEntity<>(
                publicCommentService.getComments(eventId, PageRequest.of(from / size, size)),
                HttpStatus.OK
        );
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<Object> getCommentByPublic(@PathVariable Long commentId) {
            log.info("Получен запрос к эндпоинту: {} /comments/{}", "GET", commentId);
            return new ResponseEntity<>(
                    publicCommentService.getComment(commentId),
                    HttpStatus.OK
            );
    }
}
