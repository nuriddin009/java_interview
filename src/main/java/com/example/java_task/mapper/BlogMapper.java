package com.example.java_task.mapper;

import com.example.java_task.dto.BlogDto;
import com.example.java_task.entities.Blog;

public class BlogMapper {

    public static Blog mapToBlog(BlogDto blogDto){
        return Blog.builder()
                .title(blogDto.getTitle())
                .topic(blogDto.getTopic())
                .text(blogDto.getText())
                .checked(false)
                .build();
    }
}
