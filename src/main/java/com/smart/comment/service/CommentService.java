package com.smart.comment.service;

import com.smart.comment.controller.dto.CommentDto.CreateRequest;
import com.smart.comment.controller.dto.CommentDto.DeleteRequest;
import com.smart.comment.controller.dto.CommentDto.UpdateRequest;
import com.smart.comment.dao.CommentDao;
import com.smart.comment.domain.Comment;
import com.smart.global.error.NotFoundCommentException;
import com.smart.global.error.PermissionDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

  private final CommentDao commentDao;

  public CommentService(CommentDao commentDao) {
    this.commentDao = commentDao;
  }

  @Transactional
  public Long createComment(CreateRequest request, Long loginUserId) {
    Comment comment = request.toEntity(loginUserId);
    commentDao.createComment(comment);
    return comment.getCommentId();
  }

  @Transactional
  public Long updateComment(UpdateRequest request, Long loginUserId) {
    checkPermission(loginUserId, request.getUserId());
    checkExistComment(request.getCommentId());
    commentDao.updateComment(request.toEntity());
    return request.getCommentId();
  }

  @Transactional
  public void deleteByCommentId(DeleteRequest request, Long loginUserId) {
    checkPermission(loginUserId, request.getUserId());
    checkExistComment(request.getCommentId());
    commentDao.deleteByCommentId(request.getCommentId());
  }

  private void checkExistComment(Long commentId) {
    if (!commentDao.checkCommentId(commentId)) {
      throw new NotFoundCommentException();
    }
  }

  private void checkPermission(Long loginUserId, Long authorUserId) {
    if (!loginUserId.equals(authorUserId)) {
      throw new PermissionDeniedException();
    }
  }
}
