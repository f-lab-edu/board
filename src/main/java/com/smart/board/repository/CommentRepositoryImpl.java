package com.smart.board.repository;

import com.smart.board.domain.Comment;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class CommentRepositoryImpl implements CommentRepository {

  private Map<Long, Comment> comments = new HashMap<>();
  private static Long sequence = 0L;

  @Override
  public List<Comment> findByPostId(Long postId) {
    return comments.values().stream()
        .filter(comment -> comment.getPostId().equals(postId))
        .toList();
  }

  @Override
  public void deleteByPostId(Long postId) {
    this.findByPostId(postId).stream()
        .forEach(comment -> comments.remove(comment.getCommentId()));
  }

  @Override
  public Optional<Comment> findByCommentId(Long commentId) {
    return Optional.ofNullable(comments.get(commentId));
  }

  @Override
  public Long save(Comment comment) {
    comment.setCommentId(sequence++);
    comments.put(comment.getCommentId(), comment);
    return comment.getCommentId();
  }

  @Override
  public void update(Comment comment) {
    if (comments.containsKey(comment.getCommentId())) {
      comments.put(comment.getCommentId(), comment);
    }
  }

  @Override
  public void deleteByCommentId(Long commentId) {
    comments.remove(commentId);
  }

  @Override
  public boolean existsByCommentId(Long commentId) {
    return comments.containsKey(commentId);
  }
}
