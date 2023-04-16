package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.StatDto;
import ru.practicum.dto.StatShortDto;
import ru.practicum.mapper.StatMapper;
import ru.practicum.repository.StatRepository;
import ru.practicum.model.Stat;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StatServiceImpl implements StatService {
    private final StatRepository statRepository;
    private final StatMapper statMapper;

    @Autowired
    public StatServiceImpl(StatRepository statRepository, StatMapper statMapper) {
        this.statRepository = statRepository;
        this.statMapper = statMapper;
    }

    @Override
    public StatDto create(StatDto statDto) {
        Stat stat = statMapper.toStat(statDto);
        System.out.println(stat);
        return statMapper.toStatDto(statRepository.save(stat));
    }

    @Override
    public Set<StatShortDto> getStats(LocalDateTime start, LocalDateTime end, Collection<URI> uris, Boolean unique) {
        Set<StatShortDto> result;

        if (uris != null) {
            if (unique) {
                result = statRepository.findByUriInAndDistinctIpAndTimestampBetween(uris, start, end)
                        .stream()
                        .map(statMapper::toStatShortDto)
                        .collect(Collectors.toSet());
            } else {
                result = statRepository.findByUriInAndTimestampBetween(uris, start, end)
                        .stream()
                        .map(statMapper::toStatShortDto)
                        .collect(Collectors.toSet());
            }
        } else {
            if (unique) {
                result = statRepository.findByDistinctIpAndTimestampBetween(start, end)
                        .stream()
                        .map(statMapper::toStatShortDto)
                        .collect(Collectors.toSet());
            } else {
                result = statRepository.findByTimestampBetween(start, end)
                        .stream()
                        .map(statMapper::toStatShortDto)
                        .collect(Collectors.toSet());
            }
        }

        return result;
    }
}
