package com.dev.backend.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import com.dev.backend.entities.Like;
import com.dev.backend.entities.Post;
import com.dev.backend.entities.User;
import com.dev.backend.repositories.LikeRepository;
import com.dev.backend.repositories.PostRepository;
import com.dev.backend.repositories.UserRepository;
import com.dev.backend.entities.Notification;
import com.dev.backend.services.NotificationService;
import java.time.LocalDateTime;


import java.util.List;
import java.util.Optional;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    public List<Like> getAllLikes() {
        return likeRepository.findAll();
    }

    public Optional<Like> getLikeById(Long id) {
        return likeRepository.findById(id);
    }

    public Optional<Like> getLikeByUserAndPost(Long userId, Long postId) {
        return likeRepository.findByUserIdAndPostId(userId, postId);
    }

    public Like createLike(Like like) {
        return likeRepository.save(like);
    }

    public void deleteLike(Long id) {
        likeRepository.deleteById(id);
    }

    public void deleteLikeByUserAndPost(Long userId, Long postId) {
        getLikeByUserAndPost(userId, postId)
            .ifPresent(likeRepository::delete);
    }

    private User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();
    return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Utilisateur connecté non trouvé"));
    }

    @Transactional
    public int toggleLike(Long postId) {
    User user = getCurrentUser();

    Optional<Like> existing = likeRepository.findByUserIdAndPostId(user.getId(), postId);

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("Post not found"));

    if (existing.isPresent()) {
        likeRepository.delete(existing.get());
    } else {
        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        likeRepository.save(like);

        if (!post.getAuthor().getId().equals(user.getId())) {
            Notification notif = new Notification();
            notif.setUser(post.getAuthor());
            notif.setMessage(user.getFirstname() + " liked your post: " + post.getTitle());
            notif.setCreatedAt(LocalDateTime.now());
            notificationService.create(notif);
        }
    }

    return likeRepository.countByPostId(postId);
  }

}