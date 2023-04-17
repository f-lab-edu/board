package com.smart.comment.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.smart.comment.controller.dto.CommentDto;
import com.smart.comment.dao.CommentDao;
import com.smart.comment.domain.Comment;
import com.smart.global.error.NotFoundCommentException;
import com.smart.global.error.PermissionDeniedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

  CommentDao commentDao;

  CommentService commentService;

  CommentDto.CommentInfo commentInfo;

  CommentDto.UpdateRequest updateRequest;

  CommentDto.DeleteRequest deleteRequest;

  @BeforeEach
  public void inject_Mock() {
    commentDao = mock(CommentDao.class);
    commentService = new CommentService(commentDao);
  }

  @BeforeEach
  public void create_Test_Data() {
    commentInfo = CommentDto.CommentInfo.builder()
        .commentId(1L)
        .content("content")
        .boardId(1L)
        .userId(1L)
        .nickname("nickname")
        .build();

    updateRequest = CommentDto.UpdateRequest.builder()
        .commentId(1L)
        .content("update content")
        .userId(1L)
        .build();

    deleteRequest = CommentDto.DeleteRequest.builder()
        .commentId(1L)
        .userId(1L)
        .build();
  }

  @DisplayName("댓글을 생성한다.")
  @Test
  public void createComment() {
    ArgumentCaptor<Comment> createCaptor = ArgumentCaptor.forClass(Comment.class);
    doNothing().when(commentDao).createComment(createCaptor.capture());

    CommentDto.CreateRequest createRequest = CommentDto.CreateRequest
        .builder()
        .content("content")
        .build();
    commentService.createComment(createRequest, 1L);

    verify(commentDao).createComment(any(Comment.class));
    assertThat(createRequest.getContent()).isEqualTo(createCaptor.getValue().getContent());
  }

  @DisplayName("권한이 없는 댓글을 업데이트하면 PermissionDeniedBoardException을 던진다.")
  @Test
  public void updateComment_ThrowException_NotEqualToCommentUserId() {
    assertThatThrownBy(() -> commentService.updateComment(updateRequest, 2L))
        .isInstanceOf(PermissionDeniedException.class);
  }

  @DisplayName("존재하지 않는 댓글을 업데이트하면 NotFoundCommentException을 던진다.")
  @Test
  public void updateComment_ThrowException_NotExistingComment() {
    when(commentDao.checkCommentId(updateRequest.getCommentId())).thenReturn(false);

    assertThatThrownBy(() -> commentService.updateComment(updateRequest, 1L))
        .isInstanceOf(NotFoundCommentException.class);
  }

  @DisplayName("존재하는 댓글을 업데이트한다.")
  @Test
  public void updateComment_ExistingComment() {
    when(commentDao.checkCommentId(updateRequest.getCommentId())).thenReturn(true);
    ArgumentCaptor<Comment> updateCaptor = ArgumentCaptor.forClass(Comment.class);
    doNothing().when(commentDao).updateComment(updateCaptor.capture());

    Long commentId = commentService.updateComment(updateRequest, 1L);

    verify(commentDao).updateComment(any(Comment.class));
    assertThat(commentId).isEqualTo(updateCaptor.getValue().getCommentId());
    assertThat(updateRequest.getContent()).isEqualTo(updateCaptor.getValue().getContent());
  }

  @DisplayName("권한이 없는 댓글을 삭제하면 PermissionDeniedException을 던진다.")
  @Test
  public void deleteByCommentId_ThrowException_NotEqualToCommentUserId() {
    assertThatThrownBy(() -> commentService.deleteByCommentId(deleteRequest, 2L))
        .isInstanceOf(PermissionDeniedException.class);
  }

  @DisplayName("존재하지 않는 댓글을 삭제하면 NotFoundCommentException을 던진다.")
  @Test
  public void deleteByCommentId_ThrowException_NotExistingComment() {
    when(commentDao.checkCommentId(deleteRequest.getCommentId())).thenReturn(false);

    assertThatThrownBy(() -> commentService.deleteByCommentId(deleteRequest, 1L))
        .isInstanceOf(NotFoundCommentException.class);
  }

  @DisplayName("존재하는 댓글을 삭제한다.")
  @Test
  public void deleteByCommentId_ExistingBoard() {
    when(commentDao.checkCommentId(deleteRequest.getCommentId())).thenReturn(true);

    commentService.deleteByCommentId(deleteRequest, 1L);

    verify(commentDao).deleteByCommentId(deleteRequest.getCommentId());
  }
}
