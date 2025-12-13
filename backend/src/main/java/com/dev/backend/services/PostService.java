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
        return postRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Post> getPostsByAuthor(Long authorId) {
        return postRepository.findByAuthorId(authorId);
    }

    @Transactional(readOnly = true)
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Post> getPostsByAuthors(List<Long> authorIds) {
        return postRepository.findByAuthorIdIn(authorIds);
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
    public Post updatePost(Long id, Post updatedPost) {
        return postRepository.findById(id)
            .map(post -> {
                post.setTitle(updatedPost.getTitle());
                post.setContent(updatedPost.getContent());

                post.setImagePath(updatedPost.getImagePath());
                post.setVideoUrl(updatedPost.getVideoUrl());

                return postRepository.save(post);
            })
            .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
