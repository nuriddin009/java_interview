package com.example.java_task.services.impl;

import com.example.java_task.dto.BlogDto;
import com.example.java_task.entities.Blog;
import com.example.java_task.entities.Comment;
import com.example.java_task.entities.User;
import com.example.java_task.entities.enums.RoleName;
import com.example.java_task.repositories.BlogRepository;
import com.example.java_task.repositories.CommentRepository;
import com.example.java_task.repositories.LikeCommentRepository;
import com.example.java_task.repositories.UserRepository;
import com.example.java_task.security.SecurityUtil;
import com.example.java_task.services.BlogService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.java_task.mapper.BlogMapper.mapToBlog;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final LikeCommentRepository likeCommentRepository;

    @Override
    public List<Blog> getAll() {
        return blogRepository.findByCheckedOrderByCreatedAtDesc(true);
    }

    @Override
    public Blog createBlog(BlogDto blogDto) {
        String username = SecurityUtil.getSessionUser();
        Optional<User> user = userRepository.findByUsername(username);
        Blog blog = mapToBlog(blogDto);
        blog.setUser(user.get());
        return blogRepository.save(blog);
    }

    @Override
    public Blog updateBlog(Long blogId, BlogDto blogDto) {
        Optional<User> byUsername = userRepository.findByUsername(SecurityUtil.getSessionUser());
        if (byUsername.isPresent()) {
            User user = byUsername.get();
            Optional<Blog> byId = blogRepository.findById(blogId);
            if (byId.isPresent()) {
                Blog blog = byId.get();
                if (blog.getUser().equals(user)) {
                    blog.setText(blogDto.getText());
                    blog.setTopic(blogDto.getTopic());
                    blog.setTitle(blogDto.getTitle());
                    return blogRepository.save(blog);
                }
            }
        }
        return null;
    }


    @Override
    public Blog getBlog(Long blogId) {
        return null;
    }

    @Override
    public Blog makeBlogVerify(Long blogId, Boolean checked) {
        Blog blog = blogRepository.getReferenceById(blogId);
        blog.setChecked(checked);
        blogRepository.save(blog);
        return null;
    }

    @Transactional
    @Override
    public void deleteBlog(Long blogId) {
        Optional<User> byUsername = userRepository.findByUsername(SecurityUtil.getSessionUser());
        if (byUsername.isPresent()) {
            Optional<Blog> byId = blogRepository.findById(blogId);
            if (byId.isPresent()) {
                if (byId.get().getUser().equals(byUsername.get())) {
                    List<Comment> commentList = commentRepository.findAllByBlogIdOrderByCreatedAtDesc(blogId);
                    for (Comment comment : commentList) {
                        likeCommentRepository.deleteAllByCommentId(comment.getId());
                    }
                    commentRepository.deleteAll(commentList);
                    blogRepository.deleteById(blogId);
                }
            }
        }
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
