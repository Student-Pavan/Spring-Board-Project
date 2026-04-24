package com.nikhitha.gateway.repository;

import com.nikhitha.gateway.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}