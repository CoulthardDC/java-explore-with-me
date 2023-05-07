package ru.practicum.ewm.adminApi.service.comment;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.base.dto.comment.CommentDto;

import java.util.List;

public interface AdminCommentService {

    List<CommentDto> getComments(Pageable pageable);

    void deleteComment(Long commentId);
}
