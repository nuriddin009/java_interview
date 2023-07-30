package com.example.java_task.services.impl;

import com.example.java_task.dto.CommentDto;
import com.example.java_task.entities.Blog;
import com.example.java_task.entities.Comment;
import com.example.java_task.entities.User;
import com.example.java_task.repositories.BlogRepository;
import com.example.java_task.repositories.CommentRepository;
import com.example.java_task.repositories.LikeCommentRepository;
import com.example.java_task.repositories.UserRepository;
import com.example.java_task.security.SecurityUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class CommentServiceImplTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private BlogRepository blogRepository;

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private LikeCommentRepository likeCommentRepository;
    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void createComment() {
        // Arrange
        Long blogId = 1L;
        String description = "Test comment description";
        String username = "testuser";

        Blog blog = new Blog();
        blog.setId(blogId);

        User user = new User();
        user.setUsername(username);

        CommentDto commentDto = new CommentDto();
        commentDto.setDescription(description);

        when(SecurityUtil.getSessionUser()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(blogRepository.getReferenceById(blogId)).thenReturn(blog);
        when(commentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Comment result = commentService.createComment(blogId, commentDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getDescription()).isEqualTo(description);
        assertThat(result.getBlog().getId()).isEqualTo(blogId);
        assertThat(result.getUser().getUsername()).isEqualTo(username);
        assertThat(result.getLikes()).isEqualTo(0);
        assertThat(result.getDislikes()).isEqualTo(0);
        assertThat(result.getChecked()).isFalse();
    }

    @Test
    void getBlogComments() {
        // Arrange
        Long blogId = 1L;

        Comment comment1 = new Comment();
        comment1.setId(100L);
        comment1.setDescription("Test Comment 1");
        comment1.setBlog(new Blog()); // Set the blog for comment1

        Comment comment2 = new Comment();
        comment2.setId(101L);
        comment2.setDescription("Test Comment 2");
        comment2.setBlog(new Blog()); // Set the blog for comment2

        List<Comment> comments = List.of(comment1, comment2);

        when(commentRepository.findAllByBlogIdOrderByCreatedAtDesc(blogId)).thenReturn(comments);

        // Act
        List<Comment> result = commentService.getBlogComments(blogId);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(100L);
        assertThat(result.get(0).getDescription()).isEqualTo("Test Comment 1");
        assertThat(result.get(1).getId()).isEqualTo(101L);
        assertThat(result.get(1).getDescription()).isEqualTo("Test Comment 2");
    }

    @Test
    void makeCommentVerify() {
        // Arrange
        Long commentId = 1L;
        Boolean verify = true;

        Comment existingComment = new Comment();
        existingComment.setId(commentId);
        existingComment.setDescription("Test comment");
        existingComment.setChecked(false);

        when(commentRepository.getReferenceById(commentId)).thenReturn(existingComment);
        when(commentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Comment result = commentService.makeCommentVerify(commentId, verify);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(commentId);
        assertThat(result.getChecked()).isEqualTo(verify);
    }

    @Test
    void deleteComment() {
    }

    @Test
    void likeOrDislike() {
        // Arrange
        Long commentId = 1L;
        String likeType = "LIKE";
        String username = "testuser";

        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setDescription("Test comment");
        comment.setLikes(0);
        comment.setDislikes(0);

        User user = new User();
        user.setUsername(username);

        when(SecurityUtil.getSessionUser()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(commentRepository.getReferenceById(commentId)).thenReturn(comment);
        when(likeCommentRepository.findByCommentIdAndUserId(commentId, user.getId())).thenReturn(Optional.empty());
        when(likeCommentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(commentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Comment result = commentService.likeOrDislike(likeType, commentId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(commentId);
        assertThat(result.getLikes()).isEqualTo(1);
        assertThat(result.getDislikes()).isEqualTo(0);

        // Reset the comment likes and dislikes for the next test
        comment.setLikes(0);
        comment.setDislikes(0);

        // Test Dislike
        likeType = "DISLIKE";
        result = commentService.likeOrDislike(likeType, commentId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(commentId);
        assertThat(result.getLikes()).isEqualTo(0);
        assertThat(result.getDislikes()).isEqualTo(1);
    }

    @Test
    void isModerator() {
    }
}