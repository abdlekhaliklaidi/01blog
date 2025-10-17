package com.dev.backend.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.dev.backend.entities.Like;
import com.dev.backend.repositories.LikeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

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
            .ifPresent(like -> likeRepository.delete(like));
    }
}
