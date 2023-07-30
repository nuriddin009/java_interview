package com.example.java_task.services;

import com.example.java_task.dto.BlogDto;
import com.example.java_task.entities.Blog;

import java.util.List;

public interface BlogService {

    List<Blog> getAll();
    Blog createBlog(BlogDto blogDto);
    Blog updateBlog(Long blogId, BlogDto blogDto);
    Blog getBlog(Long blogId);
    Blog makeBlogVerify(Long blogId, Boolean checked);
    void deleteBlog(Long blogId);
    boolean isModerator();


}
