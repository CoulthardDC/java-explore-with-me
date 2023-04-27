package ru.practicum.controller.cadmin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/admin/categories")
public class AdminCategoriesController {

    @PostMapping
    public ResponseEntity<Object> create() {

    }
}
