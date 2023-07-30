package com.example.java_task.controllers;

import com.example.java_task.dto.BlogDto;
import com.example.java_task.entities.Blog;
import com.example.java_task.entities.User;
import com.example.java_task.repositories.BlogRepository;
import com.example.java_task.repositories.UserRepository;
import com.example.java_task.security.SecurityUtil;
import com.example.java_task.services.BlogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequiredArgsConstructor
public class BlogController {

    private final BlogService service;
    private final UserRepository userRepository;
    private final BlogRepository blogRepository;

    @GetMapping("blogs")
    public String getBlogs(Model model) {
        model.addAttribute("blogs", service.getAll());
        User user = userRepository.findByUsername(SecurityUtil.getSessionUser()).get();
        model.addAttribute("user", user);
        return "blogs";
    }


    // Bloglarni tasdiqlash uchun
    @GetMapping("/verify_blog")
    public String verifyBlogPage(Model model) {
        if (service.isModerator()) {
            model.addAttribute("blogs", blogRepository.findAllByOrderByCreatedAtDesc());
            return "moderator-blog";
        }
        return "redirect:/login";
    }


    @GetMapping("create_blog")
    public String createBlog(Model model) {
        Blog blog = new Blog();
        model.addAttribute("blog", blog);
        return "create-blog";
    }


    @PostMapping("blog/new")
    public String saveBlog(@Valid @ModelAttribute("blog") BlogDto blogDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("blog", blogDto);
            return "create-blog";
        }
        service.createBlog(blogDto);
        return "redirect:/blogs";
    }

    @GetMapping("blogs/{blogId}")
    public String deleteBlog(@PathVariable Long blogId) {
        service.deleteBlog(blogId);
        return "redirect:/blogs";
    }

    @GetMapping("moderator/{blogId}")
    public String moderatorDeleteBlog(@PathVariable Long blogId) {
        if (service.isModerator()) {
            service.deleteBlog(blogId);
            return "redirect:/verify_blog";
        }
        return "redirect:/login";
    }


    @GetMapping("blogs/{blogId}/{verify}")
    public String verifyBlog(
            @PathVariable Long blogId,
            @PathVariable Boolean verify, Model model
    ) {
        service.makeBlogVerify(blogId, verify);
        model.addAttribute("blogs", blogRepository.findAllByOrderByCreatedAtDesc());
        return "moderator-blog";
    }


    @GetMapping("blogs/{blogId}/edit")
    public String updateBlogForm(@PathVariable Long blogId, Model model) {
        model.addAttribute("blog", blogRepository.getReferenceById(blogId));
        return "blogs-edit";
    }

    @PostMapping("blogs/{blogId}/edit")
    public String updateBlog(
            @PathVariable Long blogId,
            @ModelAttribute("blog") BlogDto blogDto,
            BindingResult result, Model model
    ) {
        if (result.hasErrors()) {
            model.addAttribute("blog", blogDto);
            return "blogs-edit";
        }
        service.updateBlog(blogId, blogDto);
        return "redirect:/blogs";
    }


}
