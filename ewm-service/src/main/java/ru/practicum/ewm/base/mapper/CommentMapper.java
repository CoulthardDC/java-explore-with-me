package ru.practicum.ewm.base.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.base.dto.comment.CommentDto;
import ru.practicum.ewm.base.model.Comment;

@Mapper(componentModel = "spring")
public abstract class CommentMapper {

    @Mapping(source = "event.id", target = "eventId")
    public abstract CommentDto toCommentDto(Comment comment);
}
