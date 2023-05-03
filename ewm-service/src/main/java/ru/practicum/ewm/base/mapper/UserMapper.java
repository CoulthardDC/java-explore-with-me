package ru.practicum.ewm.base.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm.base.dto.user.NewUserRequest;
import ru.practicum.ewm.base.dto.user.UserDto;
import ru.practicum.ewm.base.dto.user.UserShortDto;
import ru.practicum.ewm.base.model.User;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    public abstract User toUser(NewUserRequest newUserRequest);

    public abstract UserDto toUserDto(User user);

    public abstract UserShortDto toUserShortDto(User user);
}
