package ru.practicum.ewm.base.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm.base.dto.location.LocationDto;
import ru.practicum.ewm.base.model.Location;

@Mapper(componentModel = "spring")
public abstract class LocationMapper {
    public abstract Location toLocation(LocationDto locationDto);

    public abstract LocationDto toLocationDto(Location location);

}
