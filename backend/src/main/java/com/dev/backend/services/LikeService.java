package com.dev.backend.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.dev.backend.entities.Like;
import com.dev.backend.entities.Post;
import com.dev.backend.entities.User;
import com.dev.backend.repositories.LikeRepository;
import com.dev.backend.repositories.PostRepository;
import com.dev.backend.repositories.UserRepository;

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

    public Like toggleLike(Long userId, Long postId) {
        Optional<Like> existing = likeRepository.findByUserIdAndPostId(userId, postId);

        if (existing.isPresent()) {
            likeRepository.delete(existing.get());
            return null;
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec ID : " + userId));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post non trouvé avec ID : " + postId));

        Like like = new Like();
        like.setUser(user);
        like.setPost(post);

        return likeRepository.save(like);
    }
}