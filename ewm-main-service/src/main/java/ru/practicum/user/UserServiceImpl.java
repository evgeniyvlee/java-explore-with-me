package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.exception.ExceptionMessages;
import ru.practicum.exception.ValidationException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;
import ru.practicum.util.PageSettings;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDto create(UserDto userDto) {
        validate(userDto);
        User user = userRepository.save(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> get(Long[] userIds, Integer from, Integer size) {
        Pageable pageable = new PageSettings(from, size, UserRepository.SORT_USER_ID_ASC);
        List<UserDto> userDtoList = new ArrayList<>();
        if (userIds == null) {
            userDtoList = userRepository.findAll(pageable).stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        } else {
            userDtoList = userRepository.findAllByIdIn(userIds, pageable).stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        }
        return userDtoList;
    }

    @Transactional
    @Override
    public void delete(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.USER_NOT_FOUND));
        userRepository.deleteById(userId);
    }

    @Transactional(readOnly = true)
    private void validate(UserDto userDto) {
        if (userRepository.findByName(userDto.getName()).isPresent()) {
            throw new ValidationException(ExceptionMessages.USER_NAME_CONFLICT);
        }
    }
}
