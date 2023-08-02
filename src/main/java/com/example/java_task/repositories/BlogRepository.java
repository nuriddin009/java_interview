package com.example.java_task.repositories;

import com.example.java_task.entities.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

    List<Blog> findAllByOrderByCreatedAtDesc();

    List<Blog> findAllByCheckedOrderByCreatedAtDesc(boolean check);



}
