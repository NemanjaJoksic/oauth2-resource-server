package org.joksin.oauth2resourceserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.joksin.oauth2resourceserver.model.Post;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class PostController {

    private int lastId = 1;
    private Map<Integer, Post> posts = new HashMap<>();

    @GetMapping("/api/posts")
    public ResponseEntity<List<Post>> getPosts() {
        printPrincipal();
        return new ResponseEntity(posts.values(), HttpStatus.OK);
    }

    @PostMapping("/api/posts")
    public ResponseEntity<String> createPost(@RequestBody Post post) {
        printPrincipal();
        int postId = lastId++;
        post.setId(postId);
        posts.put(postId, post);
        return new ResponseEntity("Post created. ID: " + postId, HttpStatus.OK);
    }

    @DeleteMapping("/api/posts/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Integer postId) {
        printPrincipal();
        posts.remove(postId);
        return new ResponseEntity("Post with ID " + postId + " is deleted", HttpStatus.OK);
    }

    private void printPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String principalName = authentication.getName();
        log.info("principal: {}", principal);
        log.info("principal_name: {}", principalName);
    }

}
