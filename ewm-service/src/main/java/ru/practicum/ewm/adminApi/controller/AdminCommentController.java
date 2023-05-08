package ru.practicum.ewm.adminApi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.adminApi.service.comment.AdminCommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@Slf4j
@RequestMapping("/admin/comments")
@Validated
public class AdminCommentController {

    private final AdminCommentService adminCommentService;

    @Autowired
    public AdminCommentController(AdminCommentService adminCommentService) {
        this.adminCommentService = adminCommentService;
    }

    @GetMapping
    public ResponseEntity<Object> getCommentsById(@PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                                  @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Получен запрос к эндпоинту: {} /admin/comments");
        return new ResponseEntity<>(
                adminCommentService.getComments(PageRequest.of(from / size, size)),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long commentId) {
        adminCommentService.deleteComment(commentId);
    }
}
