package com.nikhitha.gateway.repository;

import com.nikhitha.gateway.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;   // ✅ import should be here

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostId(Long postId);

}