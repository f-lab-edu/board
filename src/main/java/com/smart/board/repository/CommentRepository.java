package com.smart.board.repository;

import com.smart.board.controller.dto.comment.CommentReadDto;
import com.smart.board.domain.Comment;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentRepository {

  List<CommentReadDto> findByPostId(Long postId);

  void deleteByPostId(Long postId);

  Optional<CommentReadDto> findByCommentId(Long commentId);

  void save(Comment comment);

  void update(Comment comment);

  void deleteByCommentId(Long commentId);

  boolean existsByCommentId(Long commentId);
}
