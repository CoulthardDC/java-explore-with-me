package ru.practicum.ewm.adminApi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.adminApi.service.compilation.AdminCompilationService;
import ru.practicum.ewm.base.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.base.dto.compilation.UpdateCompilationRequest;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/admin/compilations")
public class AdminCompilationsController {
    private final AdminCompilationService adminCompilationService;

    @Autowired
    public AdminCompilationsController(AdminCompilationService adminCompilationService) {
        this.adminCompilationService = adminCompilationService;
    }

    @PostMapping()
    public ResponseEntity<Object> save(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.info("Получен запрос к эндпоинту: {} /admin/compilations", "POST");
        return new ResponseEntity<>(
                adminCompilationService.save(newCompilationDto),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Object> delete(@PathVariable Long compId) {
        log.info("Получен запрос к эндпоинту: {} /admin/compilations/{}", "DELETE", compId);
        adminCompilationService.delete(compId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<Object> update(@PathVariable Long compId,
                                                 @RequestBody @Valid UpdateCompilationRequest updateCompilationRequest) {
        log.info("Получен запрос к эндпоинту: {} /admin/compilations/{}", "PATCH", compId);
        return new ResponseEntity<>(
                adminCompilationService.update(compId, updateCompilationRequest),
                HttpStatus.OK
        );
    }
}
