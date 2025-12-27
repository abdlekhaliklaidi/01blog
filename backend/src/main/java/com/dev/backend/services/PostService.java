package com.dev.backend.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.dev.backend.entities.Post;
import com.dev.backend.entities.User;
import com.dev.backend.repositories.PostRepository;
import com.dev.backend.repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Post> getAllPosts() {
        // return postRepository.findAll();
        return postRepository.findAllWithLikesAndComments();
    }

    @Transactional(readOnly = true)
    public List<Post> getPostsByAuthor(Long authorId) {
        // return postRepository.findByAuthorId(authorId);
        return postRepository.findByAuthorIdOrderByCreatedAtDesc(authorId);
    }

    @Transactional(readOnly = true)
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Post> getPostsByAuthors(List<Long> authorIds) {
        // return postRepository.findByAuthorIdIn(authorIds);
        return postRepository.findByAuthorIdInOrderByCreatedAtDesc(authorIds);
    }
    
    @Transactional
    public Post createPost(Post post) {
        User author = userRepository.findById(post.getAuthor().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        post.setAuthor(author);
        post.setCreatedAt(LocalDateTime.now());

        return postRepository.save(post);
    }

    @Transactional
    public Post updatePost(Long id, Post updatedPost, String currentEmail) {

    Post post = postRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Post not found"));

    if (!post.getAuthor().getEmail().equals(currentEmail)) {
        throw new RuntimeException("Unauthorized");
    }

    post.setTitle(updatedPost.getTitle());
    post.setContent(updatedPost.getContent());
    post.setImagePath(updatedPost.getImagePath());
    post.setVideoUrl(updatedPost.getVideoUrl());

    return postRepository.save(post);
    }

    @Transactional
    public void deletePost(Long id, String currentEmail) {

    Post post = postRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Post not found"));

    if (!post.getAuthor().getEmail().equals(currentEmail)) {
        throw new RuntimeException("Unauthorized");
    }

    postRepository.delete(post);
    }
    
    @Transactional
    public void deletePostAsAdmin(Long id) {
    postRepository.deleteById(id);
    }
}
