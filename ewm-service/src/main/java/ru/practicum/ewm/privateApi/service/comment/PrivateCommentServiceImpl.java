package ru.practicum.ewm.privateApi.service.comment;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.adminApi.service.user.AdminUserService;
import ru.practicum.ewm.base.dto.comment.CommentDto;
import ru.practicum.ewm.base.dto.comment.NewCommentDto;
import ru.practicum.ewm.base.enums.State;
import ru.practicum.ewm.base.exception.ConflictException;
import ru.practicum.ewm.base.exception.NotFoundException;
import ru.practicum.ewm.base.mapper.CommentMapper;
import ru.practicum.ewm.base.model.Comment;
import ru.practicum.ewm.base.model.Event;
import ru.practicum.ewm.base.model.User;
import ru.practicum.ewm.base.repository.CommentRepository;
import ru.practicum.ewm.base.repository.EventRepository;
import ru.practicum.ewm.base.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PrivateCommentServiceImpl implements PrivateCommentService {

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    public PrivateCommentServiceImpl(CommentRepository commentRepository,
                                     CommentMapper commentMapper,
                                     UserRepository userRepository,
                                     EventRepository eventRepository) {
        this.commentMapper = commentMapper;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public CommentDto create(Long userId, Long eventId, NewCommentDto newCommentDto) {
        User user = getUserOrElseThrow(userRepository.findById(userId));
        Event event =getEventOrElseThrow(eventRepository.findById(eventId));

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Создавать комментарии можно только к опубликованным событиям.");
        }

        Comment comment = Comment.builder()
                .text(newCommentDto.getText())
                .author(user)
                .event(event)
                .createdOn(LocalDateTime.now())
                .build();
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto update(Long userId, Long commentId, NewCommentDto newCommentDto) {
        getUserOrElseThrow(userRepository.findById(userId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Комментарий не найден")
        );

        isOwner(userId, comment.getAuthor().getId());

        comment.setText(newCommentDto.getText());
        comment.setEditedOn(LocalDateTime.now());

        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentDto> getComments(Long userId, Long eventId, Pageable pageable) {
        getUserOrElseThrow(userRepository.findById(userId));

        List<Comment> comments;
        if (eventId != null) {

            getEventOrElseThrow((eventRepository.findById(eventId)));

            comments = commentRepository.findAllByAuthorIdAndEventId(userId, eventId);
        } else {
            comments = commentRepository.findAllByAuthorId(userId);
        }

        return comments.stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {
        getUserOrElseThrow(userRepository.findById(userId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Комментарий не найден")
        );

        isOwner(userId, comment.getAuthor().getId());
        commentRepository.deleteById(commentId);
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

    private void isOwner(Long ownerId, Long userId) {
        if (!Objects.equals(ownerId, userId)) {
            throw new ConflictException("Пользователь не является владельцем");
        }
    }
}
