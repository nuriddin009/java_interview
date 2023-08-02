package com.example.java_task.repositories;

import com.example.java_task.entities.Comment;
import com.example.java_task.entities.LikeComment;
import com.example.java_task.entities.User;
import com.example.java_task.entities.enums.LikeEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

@DataJpaTest
class LikeCommentRepositoryTest {

    @Autowired
    private LikeCommentRepository likeCommentRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void findByCommentIdAndUserId() {
        // Arrange
        Comment comment = new Comment();
        comment.setDescription("Test comment");
        Comment savedComment = commentRepository.save(comment);

        User user = new User();
        user.setUsername("testuser");
        User savedUser = userRepository.save(user);

        LikeComment like1 = new LikeComment();
        like1.setComment(savedComment);
        like1.setUser(savedUser);
        like1.setLikeEnum(LikeEnum.LIKE);
        likeCommentRepository.save(like1);

        LikeComment like2 = new LikeComment();
        like2.setComment(savedComment);
        like2.setUser(savedUser);
        like2.setLikeEnum(LikeEnum.DISLIKE);
        likeCommentRepository.save(like2);

        // Act
        LikeComment foundLike = likeCommentRepository.findByCommentAndUser(savedComment, savedUser).get(0);

        // Assert
        assertThat(foundLike).isNotNull();
        assertThat(foundLike.getUser()).isEqualTo(savedUser);
        assertThat(foundLike.getLikeEnum()).isEqualTo(LikeEnum.LIKE);
    }



    @Test
    void deleteAllByCommentId() {
        // Arrange
        Comment comment = new Comment();
        comment.setDescription("Test comment");
        Comment savedComment = commentRepository.save(comment);

        LikeComment like1 = new LikeComment();
        like1.setComment(savedComment);
        like1.setUser(new User());
        like1.setLikeEnum(LikeEnum.LIKE);
        likeCommentRepository.save(like1);

        LikeComment like2 = new LikeComment();
        like2.setComment(savedComment);
        like2.setUser(new User());
        like2.setLikeEnum(LikeEnum.DISLIKE);
        likeCommentRepository.save(like2);

        // Act
        likeCommentRepository.deleteAllByCommentId(savedComment.getId());

        // Assert
        List<LikeComment> likes = likeCommentRepository.findAll();
        assertThat(likes).isEmpty();
    }
}