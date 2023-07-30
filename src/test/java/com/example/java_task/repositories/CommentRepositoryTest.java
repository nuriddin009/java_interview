package com.example.java_task.repositories;

import com.example.java_task.entities.Blog;
import com.example.java_task.entities.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Test
    void findAllByBlogIdOrderByCreatedAtDesc() {
        // Arrange
        Long blogId = 1L;

        Blog blog = new Blog();
        blog.setId(blogId);
        blog.setTitle("Sample Blog");
        // Save the blog to the database before creating comments with a reference to it
        blogRepository.save(blog);

        Comment comment1 = new Comment();
        comment1.setBlog(blog);
        comment1.setDescription("Comment 1");
        commentRepository.save(comment1);

        Comment comment2 = new Comment();
        comment2.setBlog(blog);
        comment2.setDescription("Comment 2");
        commentRepository.save(comment2);

        // Act
        List<Comment> comments = commentRepository.findAllByBlogIdOrderByCreatedAtDesc(blog.getId());

        // Assert
        assertThat(comments).isNotEmpty();
        assertThat(comments).hasSize(2);
        assertThat(comments.get(0).getDescription()).isEqualTo("Comment 2");
        assertThat(comments.get(1).getDescription()).isEqualTo("Comment 1");
    }
}