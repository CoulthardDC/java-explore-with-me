package ru.practicum.ewm.adminApi.service.compilation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.base.repository.CompilationRepository;
import ru.practicum.ewm.base.repository.EventRepository;
import ru.practicum.ewm.base.dto.compilation.CompilationDto;
import ru.practicum.ewm.base.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.base.dto.compilation.UpdateCompilationRequest;
import ru.practicum.ewm.base.exception.ConflictException;
import ru.practicum.ewm.base.exception.NotFoundException;
import ru.practicum.ewm.base.mapper.CompilationMapper;
import ru.practicum.ewm.base.model.Compilation;
import ru.practicum.ewm.base.model.Event;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
@Transactional(readOnly = true)
public class AdminCompilationServiceImpl implements AdminCompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    private final CompilationMapper compilationMapper;

    public AdminCompilationServiceImpl(CompilationRepository compilationRepository,
                                       EventRepository eventRepository,
                                       CompilationMapper compilationMapper) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
        this.compilationMapper = compilationMapper;
    }

    @Transactional
    @Override
    public CompilationDto save(NewCompilationDto newCompilationDto) {
        Compilation compilation = compilationMapper.toCompilation(newCompilationDto);
        compilation.setEvents(findEvents(newCompilationDto.getEvents()));
        try {
            compilation = compilationRepository.save(compilation);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage(), e);
        }
        log.info("Add compilation: {}", compilation.getTitle());
        return compilationMapper.toCompilationDto(compilation);
    }

    @Transactional
    @Override
    public void delete(Long compId) {
        if (compilationRepository.existsById(compId)) {
            log.info("Deleted compilation with id = {}", compId);
            compilationRepository.deleteById(compId);
        }
    }

    @Transactional
    @Override
    public CompilationDto update(Long compId, UpdateCompilationRequest dto) {
        Compilation compilationTarget = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation not found with id = %s", compId)));

        BeanUtils.copyProperties(dto, compilationTarget, "events", "pinned", "title");

        compilationTarget.setEvents(findEvents(dto.getEvents()));
        try {
            compilationRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage(), e);
        }

        log.info("Update category: {}", compilationTarget.getTitle());
        return compilationMapper.toCompilationDto(compilationTarget);
    }

    private List<Event> findEvents(Set<Long> eventsId) {
        if (eventsId == null) {
            return List.of();
        }
        return eventRepository.findAllByIdIn(eventsId);
    }
}
