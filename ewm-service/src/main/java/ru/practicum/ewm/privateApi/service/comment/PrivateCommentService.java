package ru.practicum.ewm.privateApi.service.comment;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.base.dto.comment.CommentDto;
import ru.practicum.ewm.base.dto.comment.NewCommentDto;

import java.util.List;

public interface PrivateCommentService {

    CommentDto create(Long userId, Long eventId, NewCommentDto newCommentDto);

    CommentDto update(Long userId, Long commentId, NewCommentDto newCommentDto);

    List<CommentDto> getComments(Long userId, Long eventId, Pageable pageable);

    void deleteComment(Long userId, Long commentId);
}
