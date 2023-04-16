package com.smart.comment.dao;

import com.smart.comment.controller.dto.CommentDto.CommentInfo;
import com.smart.comment.domain.Comment;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentDao {

  List<CommentInfo> getCommentsByBoardId(Long boardId);

  void deleteByBoardId(Long boardId);

  Optional<CommentInfo> getCommentByCommentId(Long commentId);

  void createComment(Comment comment);

  void updateComment(Comment comment);

  void deleteByCommentId(Long commentId);

  boolean checkCommentId(Long commentId);
}
