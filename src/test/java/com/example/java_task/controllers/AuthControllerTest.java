package com.example.java_task.controllers;

import com.example.java_task.dto.UserDto;
import com.example.java_task.entities.User;
import com.example.java_task.repositories.UserRepository;
import com.example.java_task.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testRegisterSuccess() throws Exception {
        UserDto userDto = UserDto.builder()
                .username("newuser")
                .password("password123")
                .firstname("John")
                .lastname("Doe")
                .build();


        // Mock userRepository.findByUsername
        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername(userDto.getUsername());
        savedUser.setFirstname(userDto.getFirstname());
        savedUser.setLastname(userDto.getLastname());

        // Mock userService.saveUser
//        when(userService.saveUser(ArgumentMatchers.any(UserDto.class))).thenReturn(savedUser);

        mockMvc.perform(post("/register/save")
                        .flashAttr("user", userDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void testRegisterDuplicateUsername() throws Exception {
        UserDto userDto = UserDto.builder()
                .username("existinguser")
                .password("password123")
                .firstname("Ketmonjon")
                .lastname("Teshajonov")
                .build();

        User existingUser = new User();
        existingUser.setId(2L);
        existingUser.setUsername("existinguser");
        existingUser.setFirstname("Jane");
        existingUser.setLastname("Smith");

        // Mock userRepository.findByUsername to return an existing user
        when(userRepository.findByUsername("existinguser")).thenReturn(Optional.of(existingUser));

        mockMvc.perform(post("/register/save")
                        .flashAttr("user", userDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register?fail"));
    }
}