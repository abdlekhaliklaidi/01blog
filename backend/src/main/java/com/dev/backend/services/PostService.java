package com.dev.backend.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.dev.backend.entities.Post;
import com.dev.backend.entities.User;
import com.dev.backend.repositories.PostRepository;
import com.dev.backend.repositories.UserRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public List<Post> getPostsByAuthor(Long authorId) {
        return postRepository.findByAuthorId(authorId);
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public Post createPost(Post post) {
        User author = userRepository.findById(post.getAuthor().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        post.setAuthor(author);
        post.setCreatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }

    public Post createPost(Post post, MultipartFile image) throws IOException {
        User author = userRepository.findById(post.getAuthor().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        post.setAuthor(author);

        if (image != null && !image.isEmpty()) {
            String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
            post.setImageBase64(base64Image);
        }

        post.setCreatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }

    public Post createPostWithImage(Post post, MultipartFile image) throws IOException {
    if (image != null && !image.isEmpty()) {
        String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
        post.setImageBase64(base64Image);
    }
    post.setCreatedAt(LocalDateTime.now());
    return postRepository.save(post);
    }


    public Post updatePost(Long id, Post updatedPost) {
        return postRepository.findById(id)
            .map(post -> {
                post.setTitle(updatedPost.getTitle());
                post.setContent(updatedPost.getContent());
                post.setImageBase64(updatedPost.getImageBase64());
                return postRepository.save(post);
            })
            .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}