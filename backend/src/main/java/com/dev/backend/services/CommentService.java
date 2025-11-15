package com.dev.backend.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.dev.backend.entities.Comment;
import com.dev.backend.repositories.CommentRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public List<Comment> getCommentsByPost(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public List<Comment> getCommentsByUser(Long userId) {
        return commentRepository.findByUserId(userId);
    }

    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }
    
    @Transactional
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment updateComment(Long id, Comment updatedComment) {
        return commentRepository.findById(id)
            .map(comment -> {
                comment.setContent(updatedComment.getContent());
                return commentRepository.save(comment);
            })
            .orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
