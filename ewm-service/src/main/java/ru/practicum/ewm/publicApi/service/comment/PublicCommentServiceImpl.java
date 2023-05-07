package ru.practicum.ewm.publicApi.service.comment;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.base.dto.comment.CommentDto;
import ru.practicum.ewm.base.exception.NotFoundException;
import ru.practicum.ewm.base.mapper.CommentMapper;
import ru.practicum.ewm.base.model.Comment;
import ru.practicum.ewm.base.model.Event;
import ru.practicum.ewm.base.model.User;
import ru.practicum.ewm.base.repository.CommentRepository;
import ru.practicum.ewm.base.repository.EventRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PublicCommentServiceImpl implements PublicCommentService {
    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    private final EventRepository eventRepository;

    public PublicCommentServiceImpl(CommentRepository commentRepository,
                                    CommentMapper commentMapper,
                                    EventRepository eventRepository) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.eventRepository = eventRepository;
    }

    @Override
    public List<CommentDto> getComments(Long eventId, Pageable pageable) {
        getEventOrElseThrow(eventRepository.findById(eventId));

        return commentRepository.findAllByEventId(eventId, pageable).stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto getComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Комментарий не найден")
        );
        return commentMapper.toCommentDto(comment   );
    }

    private User getUserOrElseThrow(Optional<User> optionalUser) {
        return optionalUser.orElseThrow(
                () -> new NotFoundException("Пользователь не найден")
        );
    }

    private Event getEventOrElseThrow(Optional<Event> optionalEvent) {
        return optionalEvent.orElseThrow(
                () -> new NotFoundException("Эвент не найден")
        );
    }
}
