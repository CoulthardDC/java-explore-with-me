package ru.practicum.ewm.adminApi.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.adminApi.service.user.AdminUserService;
import ru.practicum.ewm.base.dto.user.NewUserRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/users")
@Validated
public class AdminUsersController {

    private final AdminUserService adminUserService;

    @Autowired
    public AdminUsersController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping()
    public ResponseEntity<Object> getAll(@RequestParam(required = false) List<Long> ids,
                                                @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос к эндпоинту: {} /admin/users", "GET");
        return new ResponseEntity<>(
                adminUserService.getAll(ids, from, size),
                HttpStatus.OK
        );
    }

    @PostMapping()
    public ResponseEntity<Object> save(@RequestBody @Valid NewUserRequest newUserRequest) {
        log.info("Получен зпрос к эндпоинту: {} /admin/users", "POST");
        return new ResponseEntity<>(
                adminUserService.save(newUserRequest),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(@PathVariable Long userId) {
        log.info("Получен запрос к эндпоинту: {} /admin/users/{}", "DELETE", userId);
        adminUserService.delete(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
