package ru.practicum.mapper;


import org.mapstruct.Mapper;
import ru.practicum.dto.StatDto;
import ru.practicum.dto.StatShortDto;
import ru.practicum.model.Stat;

@Mapper(componentModel = "spring")
public abstract class StatMapper {
    public abstract Stat toStat(StatDto statDto);

    public abstract StatDto toStatDto(Stat stat);

    public abstract StatShortDto toStatShortDto(Stat stat);
}