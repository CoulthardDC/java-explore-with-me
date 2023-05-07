package ru.practicum.ewm.publicApi.service.comment;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.base.dto.comment.CommentDto;

import java.util.List;

public interface PublicCommentService {
    List<CommentDto> getComments(Long eventId, Pageable pageable);

    CommentDto getComment(Long commentId);
}
