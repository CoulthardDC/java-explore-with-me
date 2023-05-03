package ru.practicum.ewm.adminApi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.adminApi.service.category.AdminCategoriesService;
import ru.practicum.ewm.base.dto.Category.NewCategoryDto;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/admin/categories")
public class AdminCategoriesController {

    public final AdminCategoriesService adminCategoriesService;

    @Autowired
    public AdminCategoriesController(AdminCategoriesService adminCategoriesService) {
        this.adminCategoriesService = adminCategoriesService;
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("Получен запрос к эндпоинту: {} /admin/categories", "POST");
        return new ResponseEntity<>(
                adminCategoriesService.create(newCategoryDto),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Object> delete(@PathVariable Long catId) {
        log.info("Получен запрос к эндпоинту: {} /admin/categories/{}", "DELETE", catId);
        adminCategoriesService.delete(catId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<Object> update(@RequestBody @Valid NewCategoryDto newCategoryDto,
                                              @PathVariable Long catId) {
        log.info("Получен запрос к эндпоинту: {} /admin/categories/{}", "PATCH", catId);
        return new ResponseEntity<>(
                adminCategoriesService.update(newCategoryDto, catId),
                HttpStatus.OK
        );
    }
}
