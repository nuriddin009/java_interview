package com.example.java_task.repositories;

import com.example.java_task.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByBlogIdOrderByCreatedAtDesc(Long blogId);



}
