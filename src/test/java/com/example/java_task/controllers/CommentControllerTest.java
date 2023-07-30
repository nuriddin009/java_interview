package com.example.java_task.controllers;

import com.example.java_task.dto.CommentDto;
import com.example.java_task.entities.Blog;
import com.example.java_task.entities.Comment;
import com.example.java_task.repositories.BlogRepository;
import com.example.java_task.services.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @Mock
    private BlogRepository blogRepository;

    @InjectMocks
    private CommentController commentController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
    }

    @Test
    void getPostComments() throws Exception {
        Long blogId = 1L;
        Blog blog = new Blog();
        blog.setId(blogId);
        List<Comment> comments = new ArrayList<>();
        Comment comment1 = new Comment();
        comment1.setId(1L);
        Comment comment2 = new Comment();
        comment2.setId(2L);
        comments.add(comment1);
        comments.add(comment2);

        when(blogRepository.findById(blogId)).thenReturn(Optional.of(blog));
        when(commentService.getBlogComments(blogId)).thenReturn(comments);

        mockMvc.perform(get("/comments/{blogId}", blogId))
                .andExpect(status().isOk())
                .andExpect(view().name("comments_post"))
                .andExpect(model().attributeExists("comments", "blog", "commentDto"));

    }

    @Test
    void postComment() throws Exception {
        Long blogId = 1L;
        CommentDto commentDto = new CommentDto();
        Comment comment = new Comment();
        comment.setId(1L);

        when(commentService.createComment(blogId, commentDto)).thenReturn(comment);

        mockMvc.perform(post("/comments/{blogId}", blogId)
                        .flashAttr("commentDto", commentDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/comments/" + blogId))
                .andExpect(model().attributeExists("comment"));
    }

    @Test
    void likeOrDislike() throws Exception {
        Long commentId = 1L;
        String likeType = "like";

        Comment comment = new Comment();
        comment.setId(commentId);

        when(commentService.likeOrDislike(likeType, commentId)).thenReturn(comment);

        mockMvc.perform(get("/comments/{likeType}/{commentId}", likeType, commentId))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/comments/" + comment.getBlog().getId()));

    }

    @Test
    void testVerifyCommentWithModerator() throws Exception {
        Long blogId = 1L;
        Blog blog = new Blog();
        blog.setId(blogId);

        when(blogRepository.getReferenceById(blogId)).thenReturn(blog);
        when(commentService.isModerator()).thenReturn(true);

        mockMvc.perform(get("/comments/verify_comment/{blogId}", blogId))
                .andExpect(status().isOk())
                .andExpect(view().name("moderator-comment"))
                .andExpect(model().attributeExists("comments", "blog"));
    }

    @Test
    void testVerifyCommentWithVerify() throws Exception {
        Long commentId = 1L;
        Boolean verify = true;
        Comment comment = new Comment();
        comment.setId(commentId);

        when(commentService.makeCommentVerify(commentId, verify)).thenReturn(comment);
        when(comment.getBlog()).thenReturn(new Blog());

        mockMvc.perform(get("/comments/check/{commentId}/{verify}", commentId, verify))
                .andExpect(status().isOk())
                .andExpect(view().name("moderator-comment"))
                .andExpect(model().attributeExists("comments", "blog"));
    }
}