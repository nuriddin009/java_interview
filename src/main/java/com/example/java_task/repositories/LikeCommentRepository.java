package com.example.java_task.repositories;

import com.example.java_task.entities.Comment;
import com.example.java_task.entities.LikeComment;
import com.example.java_task.entities.User;
import com.example.java_task.entities.enums.LikeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeCommentRepository extends JpaRepository<LikeComment, Long> {


//    Optional<LikeComment> findByCommentIdAndUserId(Long commentId, Long userId);

    List<LikeComment> findByCommentAndUser(Comment comment, User user);

    long countByCommentIdAndLikeEnum(Long commentId, LikeEnum likeEnum);

    void deleteAllByCommentId(Long commentId);

}
