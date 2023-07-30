package com.example.java_task.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;




@Data
public class RegistrationDto {

    @NotBlank
    private String username;
    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    @NotBlank
    private String password;
}
