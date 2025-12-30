package com.dev.backend.services;

import com.dev.backend.entities.User;
import com.dev.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dev.backend.dto.AdminUserDTO;
import com.dev.backend.dto.AdminPostDTO;
import com.dev.backend.entities.Post;
import com.dev.backend.repositories.PostRepository;
import java.util.List;

@Service
public class AdminService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    public List<AdminUserDTO> getAllUsers() {
    return userRepository.findAll()
        .stream()
        .filter(user -> !user.getEmail().equals("admin@gmail.com"))
        .map(user -> new AdminUserDTO(
            user.getId(),
            user.getFirstname(),
            user.getLastname(),
            user.getEmail(),
            user.isBanned()
        ))
        .toList();
    }
    
    public List<AdminPostDTO> getAllPosts() {
    return postRepository.findAll()
        .stream()
        .map(post -> new AdminPostDTO(
            post.getId(),
            post.getTitle(),
            post.getAuthor().getFirstname() + " " + post.getAuthor().getLastname(),
            post.isHidden()
        ))
        .toList();
    }

    public void hidePost(Long postId) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("Post not found"));
    post.setHidden(true);
    postRepository.save(post);
    }

    public void unhidePost(Long postId) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("Post not found"));
    post.setHidden(false);
    postRepository.save(post);
    }

    public void banUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setBanned(true);
        userRepository.save(user);
    }

    public void unbanUser(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));

    user.setBanned(false);
    userRepository.save(user);
}
    
    public void deletePostAsAdmin(Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("Post not found"));
        postRepository.delete(post);
    }

}

