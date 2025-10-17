package com.dev.backend.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.dev.backend.entities.Post;
import com.dev.backend.repositories.PostRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

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
        return postRepository.save(post);
    }

    public Post updatePost(Long id, Post updatedPost) {
        return postRepository.findById(id)
            .map(post -> {
                post.setTitle(updatedPost.getTitle());
                post.setContent(updatedPost.getContent());
                return postRepository.save(post);
            })
            .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
