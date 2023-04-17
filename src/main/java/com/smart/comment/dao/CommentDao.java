package com.smart.comment.dao;

import com.smart.comment.controller.dto.CommentDto;
import com.smart.comment.domain.Comment;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentDao {

  List<CommentDto.CommentInfo> getCommentsByBoardId(Long boardId);

  void deleteByBoardId(Long boardId);

  Optional<CommentDto.CommentInfo> getCommentByCommentId(Long commentId);

  void createComment(Comment comment);

  void updateComment(Comment comment);

  void deleteByCommentId(Long commentId);

  boolean checkCommentId(Long commentId);
}
