package com.nikhitha.gateway.controller;

import com.nikhitha.gateway.entity.Post;
import com.nikhitha.gateway.entity.Comment;
import com.nikhitha.gateway.repository.PostRepository;
import com.nikhitha.gateway.repository.CommentRepository;
import com.nikhitha.gateway.service.ViralityService;
import com.nikhitha.gateway.service.LockService;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*") // ✅ FIX CORS
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostRepository repo;
    private final CommentRepository commentRepository;
    private final ViralityService viralityService;
    private final LockService lockService;

    public PostController(PostRepository repo,
                          CommentRepository commentRepository,
                          ViralityService viralityService,
                          LockService lockService) {
        this.repo = repo;
        this.commentRepository = commentRepository;
        this.viralityService = viralityService;
        this.lockService = lockService;
    }

    // ✅ CREATE POST
    @PostMapping
    public Post createPost(@RequestBody Post post) {
        post.setCreatedAt(LocalDateTime.now());
        return repo.save(post);
    }

    // ✅ GET ALL POSTS
    @GetMapping
    public List<Post> getAllPosts() {
        return repo.findAll();
    }

    // ✅ GET POST BY ID
    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Long id) {
        return repo.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    }

    // ✅ ADD COMMENT
    @PostMapping("/{postId}/comments")
    public Comment addComment(@PathVariable Long postId, @RequestBody Comment comment) {

        // 🔴 Check post exists
        Post post = repo.findById(postId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        // 🔴 Depth limit
        if (comment.getDepthLevel() > 20) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Depth limit exceeded"
            );
        }

        // 🔴 BOT RULES
        if ("BOT".equalsIgnoreCase(comment.getAuthorType())) {

            if (!lockService.allowBotReply(postId)) {
                throw new ResponseStatusException(
                        HttpStatus.TOO_MANY_REQUESTS,
                        "Bot limit exceeded (100 max)"
                );
            }

            if (!lockService.checkCooldown(comment.getAuthorId(), 1L)) {
                throw new ResponseStatusException(
                        HttpStatus.TOO_MANY_REQUESTS,
                        "Cooldown active"
                );
            }
        }

        // ✅ Save comment
        comment.setPostId(post.getId());
        comment.setCreatedAt(LocalDateTime.now());

        Comment saved = commentRepository.save(comment);

        // 🔥 Virality update
        if ("BOT".equalsIgnoreCase(comment.getAuthorType())) {
            viralityService.updateScore(postId, "BOT_REPLY");
        } else {
            viralityService.updateScore(postId, "HUMAN_COMMENT");
        }

        return saved;
    }

    // ✅ GET COMMENTS
    @GetMapping("/{postId}/comments")
    public List<Comment> getComments(@PathVariable Long postId) {

        // Optional validation
        repo.findById(postId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        return commentRepository.findByPostId(postId);
    }
}