package com.dev.backend.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.dev.backend.entities.Post;
import com.dev.backend.entities.User;
import com.dev.backend.repositories.PostRepository;
import com.dev.backend.repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.time.Duration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.dev.backend.entities.Follower;
import com.dev.backend.repositories.FollowerRepository;
import com.dev.backend.services.NotificationService;
import com.dev.backend.entities.Notification;


@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private FollowerRepository followerRepository;

    private static final long POST_COOLDOWN_SECONDS = 30;

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
    
    @Transactional(readOnly = true)
    public List<Post> getFeedPaginated(
        List<Long> authorIds,
        Long lastPostId,
        int limit
    ) {
    Pageable pageable = PageRequest.of(0, limit);
    return postRepository.findFeedWithPagination(authorIds, lastPostId, pageable);
    }

    // @Transactional
    // public Post createPost(Post post) {
    //     User author = userRepository.findById(post.getAuthor().getId())
    //             .orElseThrow(() -> new RuntimeException("User not found"));

    //     post.setAuthor(author);
    //     post.setCreatedAt(LocalDateTime.now());

    //     return postRepository.save(post);
    // }

    public Post createPost(Post post) {

        User author = userRepository.findById(post.getAuthor().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Post> lastPostOpt =
                postRepository.findTopByAuthorIdOrderByCreatedAtDesc(author.getId());

        if (lastPostOpt.isPresent()) {
            LocalDateTime lastPostTime = lastPostOpt.get().getCreatedAt();
            long secondsSinceLastPost =
                    Duration.between(lastPostTime, LocalDateTime.now()).getSeconds();

            if (secondsSinceLastPost < POST_COOLDOWN_SECONDS) {
                throw new RuntimeException(
                        "Please wait " +
                        (POST_COOLDOWN_SECONDS - secondsSinceLastPost) +
                        " seconds before creating another post"
                );
            }
        }

        post.setAuthor(author);
        post.setCreatedAt(LocalDateTime.now());

        List<Follower> followers = followerRepository.findByFollowingId(author.getId());
    for (Follower f : followers) {
        Notification notif = new Notification();
        notif.setUser(f.getFollower());
        notif.setMessage(author.getFirstname() + " " + author.getLastname() + "He published a new post.");
        notificationService.create(notif);
    }

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
