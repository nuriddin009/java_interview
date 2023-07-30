package com.example.java_task.services.impl;

import com.example.java_task.dto.BlogDto;
import com.example.java_task.entities.Blog;
import com.example.java_task.entities.Comment;
import com.example.java_task.entities.User;
import com.example.java_task.repositories.BlogRepository;
import com.example.java_task.repositories.CommentRepository;
import com.example.java_task.repositories.LikeCommentRepository;
import com.example.java_task.repositories.UserRepository;
import com.example.java_task.security.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class BlogServiceImplTest {

    @Mock
    private BlogRepository blogRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private LikeCommentRepository likeCommentRepository;

    @InjectMocks
    private BlogServiceImpl blogService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll() {
        // Arrange
        Blog blog1 = new Blog();
        blog1.setId(1L);
        blog1.setTitle("Blog 1");
        blog1.setChecked(true);

        Blog blog2 = new Blog();
        blog2.setId(2L);
        blog2.setTitle("Blog 2");
        blog2.setChecked(true);

        List<Blog> blogList = List.of(blog1, blog2);

        when(blogRepository.findByCheckedOrderByCreatedAtDesc(true)).thenReturn(blogList);

        // Act
        List<Blog> result = blogService.getAll();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Blog 1");
        assertThat(result.get(1).getTitle()).isEqualTo("Blog 2");
    }

    @Test
    void createBlog() {
        // Arrange
        String username = "testuser";
        BlogDto blogDto = BlogDto.builder()
                .title("Test Blog")
                .text("This is a test blog.")
                .build();

        User user = new User();
        user.setUsername(username);

        when(SecurityUtil.getSessionUser()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(blogRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Blog result = blogService.createBlog(blogDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUser().getUsername()).isEqualTo(username);
        assertThat(result.getTitle()).isEqualTo("Test Blog");
        assertThat(result.getText()).isEqualTo("This is a test blog.");
    }

    @Test
    void updateBlog() {
        // Arrange
        String username = "testuser";
        Long blogId = 1L;

        BlogDto updatedBlogDto = BlogDto.builder()
                .title("Updated Blog")
                .text("This is an updated blog.")
                .topic("This is an updated blog.")
                .build();


        User user = new User();
        user.setUsername(username);

        Blog existingBlog = new Blog();
        existingBlog.setId(blogId);
        existingBlog.setTitle("Old Blog");
        existingBlog.setText("This is the old blog text.");
        existingBlog.setUser(user);

        when(SecurityUtil.getSessionUser()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(blogRepository.findById(blogId)).thenReturn(Optional.of(existingBlog));
        when(blogRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Blog result = blogService.updateBlog(blogId, updatedBlogDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(blogId);
        assertThat(result.getTitle()).isEqualTo("Updated Blog");
        assertThat(result.getText()).isEqualTo("This is an updated blog.");
        assertThat(result.getTopic()).isEqualTo("This is an updated blog.");
    }

    @Test
    void getBlog() {

    }

    @Test
    void makeBlogVerify() {
        // Arrange
        Long blogId = 1L;
        Boolean checked = true;

        Blog existingBlog = new Blog();
        existingBlog.setId(blogId);
        existingBlog.setTitle("Test Blog");
        existingBlog.setChecked(false);

        when(blogRepository.getReferenceById(blogId)).thenReturn(existingBlog);
        when(blogRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Blog result = blogService.makeBlogVerify(blogId, checked);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(blogId);
        assertThat(result.getChecked()).isEqualTo(checked);
    }

    @Test
    void deleteBlog() {
        // Arrange
        Long blogId = 1L;
        String username = "testuser";

        Blog existingBlog = new Blog();
        existingBlog.setId(blogId);
        existingBlog.setTitle("Test Blog");
        existingBlog.setChecked(true);

        User user = new User();
        user.setUsername(username);

        List<Comment> comments = new ArrayList<>();
        Comment comment1 = new Comment();
        comment1.setId(100L);
        comment1.setDescription("Test Comment 1");
        comment1.setBlog(existingBlog);
        Comment comment2 = new Comment();
        comment2.setId(101L);
        comment2.setDescription("Test Comment 2");
        comment2.setBlog(existingBlog);
        comments.add(comment1);
        comments.add(comment2);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(blogRepository.findById(blogId)).thenReturn(Optional.of(existingBlog));
        when(commentRepository.findAllByBlogIdOrderByCreatedAtDesc(blogId)).thenReturn(comments);

        // Act
        blogService.deleteBlog(blogId);

        // Assert
        verify(likeCommentRepository, times(1)).deleteAllByCommentId(100L);
        verify(likeCommentRepository, times(1)).deleteAllByCommentId(101L);
        verify(commentRepository, times(1)).deleteAll(comments);
        verify(blogRepository, times(1)).deleteById(blogId);
    }

    @Test
    void isModerator() {
    }
}