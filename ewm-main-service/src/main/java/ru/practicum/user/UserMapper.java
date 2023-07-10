package ru.practicum.user;

import lombok.experimental.UtilityClass;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;

@UtilityClass
public class UserMapper {
    public User toUser(UserDto userDto) {
        if (userDto == null) return new User();
        return new User(userDto.getId(), userDto.getName(), userDto.getEmail());
    }

    public UserDto toUserDto(User user) {
        if (user == null) return new UserDto();
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public UserShortDto toUserShortDto(User user) {
        if (user == null) return new UserShortDto();
        return new UserShortDto(user.getId(), user.getName());
    }
}
