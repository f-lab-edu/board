package com.smart.board.dao;

import com.smart.board.controller.dto.comment.CommentReadDto;
import com.smart.board.domain.Comment;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentDao {

  List<CommentReadDto> getCommentsByPostId(Long postId);

  void deleteByPostId(Long postId);

  Optional<CommentReadDto> getCommentByCommentId(Long commentId);

  void createComment(Comment comment);

  void updateComment(Comment comment);

  void deleteByCommentId(Long commentId);

  boolean checkCommentId(Long commentId);
}
