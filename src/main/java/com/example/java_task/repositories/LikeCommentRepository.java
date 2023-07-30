package com.example.java_task.repositories;

import com.example.java_task.entities.Comment;
import com.example.java_task.entities.LikeComment;
import com.example.java_task.entities.enums.LikeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeCommentRepository extends JpaRepository<LikeComment, Long> {


    Optional<LikeComment> findByCommentIdAndUserId(Long commentId, Long userId);

    long countByCommentIdAndLikeEnum(Long commentId, LikeEnum likeEnum);

    void deleteAllByCommentId(Long commentId);

}
