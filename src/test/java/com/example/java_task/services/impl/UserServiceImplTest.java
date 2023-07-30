package com.example.java_task.services.impl;

import com.example.java_task.dto.UserDto;
import com.example.java_task.entities.User;
import com.example.java_task.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceImplTest {


    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Test
    void saveUser() {
        // Arrange
        String username = "testuser";
        String password = "testpassword";
        String firstname = "John";
        String lastname = "Doe";

        UserDto userDto = UserDto.builder()
                .username(username)
                .password(password)
                .firstname(firstname)
                .lastname(lastname)
                .build();


        when(passwordEncoder.encode(password)).thenReturn("encodedPassword"); // Mock the password encoder

        // Act
        userService.saveUser(userDto);

        // Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo(username);
        assertThat(savedUser.getFirstname()).isEqualTo(firstname);
        assertThat(savedUser.getLastname()).isEqualTo(lastname);
        assertThat(savedUser.getPassword()).isEqualTo("encodedPassword");
    }
}