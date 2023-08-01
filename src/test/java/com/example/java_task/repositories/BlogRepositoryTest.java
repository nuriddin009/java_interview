package com.example.java_task.repositories;

import com.example.java_task.entities.Blog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class BlogRepositoryTest {


    @Autowired
    private BlogRepository blogRepository;


    @Test
    void findAllByOrderByCreatedAtDesc() {
        // Arrange
        Blog blog1 = new Blog();
        blog1.setTitle("Blog 1");
        blogRepository.save(blog1);

        Blog blog2 = new Blog();
        blog2.setTitle("Blog 2");
        blogRepository.save(blog2);

        // Act
        List<Blog> blogs = blogRepository.findAllByOrderByCreatedAtDesc();

        // Assert
        assertThat(blogs).isNotEmpty();
        assertThat(blogs).hasSize(2);
        assertThat(blogs.get(0).getTitle()).isEqualTo("Blog 2");
        assertThat(blogs.get(1).getTitle()).isEqualTo("Blog 1");
    }

    @Test
    void findByCheckedOrderByCreatedAtDesc() {
        // Arrange
        Blog checkedBlog1 = new Blog();
        checkedBlog1.setTitle("Checked Blog 1");
        checkedBlog1.setChecked(true);
        blogRepository.save(checkedBlog1);

        Blog checkedBlog2 = new Blog();
        checkedBlog2.setTitle("Checked Blog 2");
        checkedBlog2.setChecked(true);
        blogRepository.save(checkedBlog2);

        Blog uncheckedBlog = new Blog();
        uncheckedBlog.setTitle("Unchecked Blog");
        checkedBlog1.setChecked(false);
        blogRepository.save(uncheckedBlog);

        // Act
        List<Blog> checkedBlogs = blogRepository.findByCheckedOrderByCreatedAtDesc(true);

        // Assert
        assertThat(checkedBlogs).isNotEmpty();
        assertThat(checkedBlogs).hasSize(2);
        assertThat(checkedBlogs.get(0).getTitle()).isEqualTo("Checked Blog 2");
        assertThat(checkedBlogs.get(1).getTitle()).isEqualTo("Checked Blog 1");

    }
}