package com.example.java_task.controllers;


import com.example.java_task.dto.CommentDto;
import com.example.java_task.entities.Blog;
import com.example.java_task.entities.Comment;
import com.example.java_task.repositories.BlogRepository;
import com.example.java_task.services.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("comments")
public class CommentController {

    private final CommentService service;
    private final BlogRepository blogRepository;

    @GetMapping("{blogId}")
    public String getPostComments(@PathVariable Long blogId, Model model) {
        Optional<Blog> byId = blogRepository.findById(blogId);
        if (byId.isPresent()) {
            List<Comment> blogComments = service.getBlogComments(blogId);
            CommentDto commentDto = new CommentDto();
            model.addAttribute("comments", blogComments);
            model.addAttribute("blog", byId.get());

            model.addAttribute("commentDto", commentDto);
            return "comments_post";
        }
        return "redirect:/blogs";
    }


    @PostMapping("{blogId}")
    public String postComment(@PathVariable Long blogId, @Valid @ModelAttribute("commentDto") CommentDto commentDto, BindingResult result, Model model) {
        Comment comment = service.createComment(blogId, commentDto);
        model.addAttribute("comment", comment);
        return "redirect:/comments/" + blogId;
    }


    @GetMapping("{likeType}/{commentId}")
    public String likeOrDislike(
            @PathVariable String likeType,
            @PathVariable Long commentId) {
        Comment comment = service.likeOrDislike(likeType, commentId);
        return "redirect:/comments/" + comment.getBlog().getId();
    }


    @GetMapping("verify_comment/{blogId}")
    public String verifyComment(@PathVariable Long blogId, Model model) {
        if (service.isModerator()) {
            Blog blog = blogRepository.getReferenceById(blogId);
            model.addAttribute("comments", service.getBlogComments(blogId));
            model.addAttribute("blog", blog);
            return "moderator-comment";
        }
        return "redirect:/login";
    }

    @GetMapping("check/{commentId}/{verify}")
    public String verifyComment(
            @PathVariable Long commentId,
            @PathVariable Boolean verify, Model model
    ) {
        Comment comment = service.makeCommentVerify(commentId, verify);

        Blog blog = comment.getBlog();
        model.addAttribute("comments",
                service.getBlogComments(blog.getId()));
        model.addAttribute("blog", blog);
        return "moderator-comment";
    }


}



