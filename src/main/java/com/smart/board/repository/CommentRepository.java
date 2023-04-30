package com.smart.board.repository;

import com.smart.board.controller.dto.comment.CommentReadDto;
import com.smart.board.domain.Comment;
import com.smart.board.domain.Post;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CommentRepository {

  List<Comment> findByPostId(Long postId);

  void deleteByPostId(Long postId);

  Optional<Comment> findByCommentId(Long commentId);

  Long save(Comment comment);

  void update(Comment comment);

  void deleteByCommentId(Long commentId);

  boolean existsByCommentId(Long commentId);

}
