package ru.practicum.service;

import ru.practicum.dto.StatDto;
import ru.practicum.dto.StatShortDto;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface StatService {
    StatDto create(StatDto statDto);

    List<StatShortDto> getStats(LocalDateTime start, LocalDateTime end, Collection<URI> uris, Boolean unique);
}
