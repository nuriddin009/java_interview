package com.example.java_task.services.impl;

import com.example.java_task.dto.UserDto;
import com.example.java_task.entities.User;
import com.example.java_task.repositories.UserRepository;
import com.example.java_task.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public void saveUser(UserDto userDto) {

        User build = User.builder()
                .password(passwordEncoder.encode(userDto.getPassword()))
                .firstname(userDto.getFirstname())
                .lastname(userDto.getLastname())
                .username(userDto.getUsername())
                .build();

        userRepository.save(build);

    }
}
