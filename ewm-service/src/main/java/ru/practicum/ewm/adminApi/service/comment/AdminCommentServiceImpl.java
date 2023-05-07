package ru.practicum.ewm.adminApi.service.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.base.dto.comment.CommentDto;
import ru.practicum.ewm.base.mapper.CommentMapper;
import ru.practicum.ewm.base.repository.CommentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminCommentServiceImpl implements AdminCommentService {

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    @Autowired
    public AdminCommentServiceImpl(CommentRepository commentRepository,
                                   CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    public List<CommentDto> getComments(Pageable pageable) {
        return commentRepository.findAll(pageable).stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
