package ru.practicum.ewm.adminApi.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.base.repository.UserRepository;
import ru.practicum.ewm.base.dto.user.NewUserRequest;
import ru.practicum.ewm.base.dto.user.UserDto;
import ru.practicum.ewm.base.exception.ConflictException;
import ru.practicum.ewm.base.mapper.UserMapper;
import ru.practicum.ewm.base.model.User;
import ru.practicum.ewm.base.util.page.MyPageRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Autowired
    public AdminUserServiceImpl(UserRepository userRepository,
                                UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserDto> getAll(List<Long> ids, Integer from, Integer size) {
        List<User> users;
        MyPageRequest pageable = new MyPageRequest(from, size, Sort.by(Sort.Direction.ASC, "id"));
        if (ids == null) {
            users = userRepository.findAll(pageable).toList();
        } else {
            users = userRepository.findAllByIdIn(ids, pageable);
        }
        log.info("Number of users: {}", users.size());
        return users.stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public UserDto save(NewUserRequest dto) {
        User user = userMapper.toUser(dto);
        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Данный email уже занят", e);
        }
        log.info("Add user: {}", user.getEmail());
        return userMapper.toUserDto(user);
    }

    @Transactional
    @Override
    public void delete(Long userId) {
        if (userRepository.existsById(userId)) {
            log.info("Deleted user with id = {}", userId);
            userRepository.deleteById(userId);
        }
    }
}
