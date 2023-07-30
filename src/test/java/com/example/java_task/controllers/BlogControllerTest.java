package com.example.java_task.controllers;

import com.example.java_task.dto.BlogDto;
import com.example.java_task.entities.Blog;
import com.example.java_task.repositories.BlogRepository;
import com.example.java_task.repositories.UserRepository;
import com.example.java_task.services.BlogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BlogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlogService blogService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BlogRepository blogRepository;

    @Test
    void getBlogs() throws Exception {
        List<Blog> blogs = new ArrayList<>();
        blogs.add(new Blog());
        blogs.add(new Blog());

        when(blogService.getAll()).thenReturn(blogs);

        mockMvc.perform(get("/blogs"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("blogs"))
                .andExpect(view().name("blogs"));
    }

    @Test
    public void testVerifyBlogPageAsModerator() throws Exception {
        when(blogService.isModerator()).thenReturn(true);

        List<Blog> blogs = new ArrayList<>();
        blogs.add(new Blog());
        blogs.add(new Blog());

        when(blogRepository.findAllByOrderByCreatedAtDesc()).thenReturn(blogs);

        mockMvc.perform(get("/verify_blog"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("blogs"))
                .andExpect(view().name("moderator-blog"));
    }

    @Test
    public void testVerifyBlogPageAsNonModerator() throws Exception {
        when(blogService.isModerator()).thenReturn(false);

        mockMvc.perform(get("/verify_blog"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void createBlog() throws Exception {
        mockMvc.perform(get("/create_blog"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("blog"))
                .andExpect(view().name("create-blog"));
    }

    @Test
    public void testSaveBlogSuccess() throws Exception {
        BlogDto blogDto = BlogDto.builder()
                .title("Test Blog")
                .text("This is a test blog.")
                .topic("This is a test blog.")
                .build();


        Blog savedBlog = new Blog();
        savedBlog.setId(1L);
        savedBlog.setTitle(blogDto.getTitle());
        savedBlog.setText(blogDto.getText());
        savedBlog.setTopic(blogDto.getTopic());

        when(blogService.createBlog(any(BlogDto.class))).thenReturn(savedBlog);

        mockMvc.perform(post("/blog/new")
                        .flashAttr("blog", blogDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/blogs"));

        verify(blogService).createBlog(any(BlogDto.class));
    }

    @Test
    public void testSaveBlogValidationError() throws Exception {
        BlogDto blogDto = BlogDto.builder().build();

        mockMvc.perform(post("/blog/new")
                        .flashAttr("blog", blogDto))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("blog"))
                .andExpect(view().name("create-blog"));
        verify(blogService, never()).createBlog(any(BlogDto.class));
    }


}