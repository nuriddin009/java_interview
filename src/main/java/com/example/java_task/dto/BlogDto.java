package com.example.java_task.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlogDto {

    @NotBlank
    private String title;
    @NotBlank
    private String topic;
    @NotBlank
    private String text;
}
