package com.example.java_task.services;

import com.example.java_task.dto.CommentDto;
import com.example.java_task.entities.Comment;

import java.util.List;

public interface CommentService {
    Comment createComment(Long blogId, CommentDto commentDto);
    List<Comment> getBlogComments(Long blogId);
    Comment makeCommentVerify(Long commentId, Boolean verify);
    void deleteComment(Long commentId);
    Comment likeOrDislike(String likeType, Long commentId);
    boolean isModerator();
}
