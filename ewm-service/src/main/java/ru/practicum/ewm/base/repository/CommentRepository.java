package ru.practicum.ewm.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.base.model.Comment;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByAuthorId(Long userId);

    List<Comment> findAllByAuthorIdAndEventId(Long userId, Long eventId);

    List<Comment> findAllByEventId(Long eventId, Pageable pageable);
}
