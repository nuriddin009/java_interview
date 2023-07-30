package com.example.java_task.services.impl;

import com.example.java_task.dto.CommentDto;
import com.example.java_task.entities.Comment;
import com.example.java_task.entities.LikeComment;
import com.example.java_task.entities.User;
import com.example.java_task.entities.enums.LikeEnum;
import com.example.java_task.entities.enums.RoleName;
import com.example.java_task.repositories.BlogRepository;
import com.example.java_task.repositories.CommentRepository;
import com.example.java_task.repositories.LikeCommentRepository;
import com.example.java_task.repositories.UserRepository;
import com.example.java_task.security.SecurityUtil;
import com.example.java_task.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;
    private final BlogRepository blogRepository;
    private final CommentRepository commentRepository;
    private final LikeCommentRepository likeCommentRepository;

    @Override
    public Comment createComment(Long blogId, CommentDto commentDto) {
        User user = userRepository.findByUsername(SecurityUtil.getSessionUser()).get();
        Comment comment = Comment.builder()
                .likes(0)
                .dislikes(0)
                .checked(false)
                .description(commentDto.getDescription())
                .blog(blogRepository.getReferenceById(blogId))
                .user(user)
                .build();
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getBlogComments(Long blogId) {
        return commentRepository.findAllByBlogIdOrderByCreatedAtDesc(blogId);
    }

    @Override
    public Comment makeCommentVerify(Long commentId, Boolean verify) {
        Comment comment = commentRepository.getReferenceById(commentId);
        comment.setChecked(verify);
        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long commentId) {

    }


    @Override
    public Comment likeOrDislike(String likeType, Long commentId) {
        Comment referenceById = commentRepository.getReferenceById(commentId);
        User user = userRepository.findByUsername(SecurityUtil.getSessionUser()).get();
        Optional<LikeComment> byCommentIdAndUserId = likeCommentRepository.findByCommentIdAndUserId(referenceById.getId(), user.getId());

        if (byCommentIdAndUserId.isPresent()) {
            LikeComment likeComment = byCommentIdAndUserId.get();

            if (likeComment.getLikeEnum().equals(LikeEnum.valueOf(likeType))) {
                likeCommentRepository.delete(likeComment);
                if (LikeEnum.valueOf(likeType).equals(LikeEnum.LIKE)) {
                    referenceById.setLikes(referenceById.getLikes() - 1);
                } else {
                    referenceById.setDislikes(referenceById.getDislikes() - 1);
                }
            } else {
                likeComment.setLikeEnum(LikeEnum.valueOf(likeType));
                if (LikeEnum.valueOf(likeType).equals(LikeEnum.LIKE)) {
                    referenceById.setLikes(referenceById.getLikes() + 1);
                    referenceById.setDislikes(referenceById.getDislikes() - 1);
                } else {
                    referenceById.setDislikes(referenceById.getDislikes() + 1);
                    referenceById.setLikes(referenceById.getLikes() - 1);
                }

            }

        } else {
            likeCommentRepository.save(LikeComment.builder()
                    .comment(referenceById)
                    .likeEnum(LikeEnum.valueOf(likeType))
                    .user(user)
                    .build());
            if (LikeEnum.valueOf(likeType).equals(LikeEnum.LIKE)) {
                referenceById.setLikes(referenceById.getLikes() + 1);
            } else {
                referenceById.setDislikes(referenceById.getDislikes() + 1);
            }
        }
        return commentRepository.save(referenceById);
    }

    @Override
    public boolean isModerator() {
        Optional<User> byUsername = userRepository.findByUsername(SecurityUtil.getSessionUser());
        if (byUsername.isPresent()) {
            User user = byUsername.get();
            return user.getRoles().stream().filter(role -> role.getAuthority().equals(RoleName.MODERATOR.name())).toList().size() > 0;
        }
        return false;
    }


}
