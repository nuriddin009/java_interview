package com.example.java_task.mapper;

import com.example.java_task.dto.UserDto;
import com.example.java_task.entities.User;

public class UserMapper {

    public static User mapToUser(UserDto userDto) {
        return User.builder()
                .firstname(userDto.getFirstname())
                .lastname(userDto.getLastname())
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .build();

    }

    public static UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }

}
