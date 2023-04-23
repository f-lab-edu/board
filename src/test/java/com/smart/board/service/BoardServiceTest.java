package com.smart.board.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.smart.board.controller.dto.BoardListDto;
import com.smart.board.controller.dto.comment.CommentCreateDto;
import com.smart.board.controller.dto.comment.CommentDeleteDto;
import com.smart.board.controller.dto.comment.CommentUpdateDto;
import com.smart.board.controller.dto.post.PostCreateDto;
import com.smart.board.controller.dto.post.PostDeleteDto;
import com.smart.board.controller.dto.post.PostReadDto;
import com.smart.board.controller.dto.post.PostUpdateDto;
import com.smart.board.dao.CommentDao;
import com.smart.board.dao.PostDao;
import com.smart.board.domain.Comment;
import com.smart.board.domain.Post;
import com.smart.global.error.NotFoundEntityException;
import com.smart.global.error.PermissionDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

  PostDao postDao;

  CommentDao commentDao;

  BoardService boardService;

  @BeforeEach
  public void inject_Mock() {
    postDao = mock(PostDao.class);
    commentDao = mock(CommentDao.class);
    boardService = new BoardService(postDao, commentDao);
  }

  @DisplayName("게시물 없다면 비어있는 게시물 리스트를 반환한다.")
  @Test
  public void getBoardList_EmptyList_NotExistingPost() {
    when(postDao.getAllPost()).thenReturn(new ArrayList<>());

    BoardListDto boardList = boardService.getBoardList();

    verify(postDao).getAllPost();
    assertThat(boardList.getPostDtos().isEmpty()).isTrue();
  }

  @DisplayName("모든 게시물들을 반환한다.")
  @Test
  public void getBoardList_BoardList_ExistingPost() {
    List<PostReadDto> postReadDtos = new ArrayList<>();
    PostReadDto postReadDto = PostReadDto.builder().build();
    postReadDtos.add(postReadDto);
    when(postDao.getAllPost()).thenReturn(postReadDtos);

    BoardListDto boardList = boardService.getBoardList();

    verify(postDao).getAllPost();
    assertThat(boardList.getPostDtos().size()).isEqualTo(postReadDtos.size());
    assertThat(postReadDto).isIn(boardList.getPostDtos());
  }

  @DisplayName("해당 ID의 게시물이 없다면 NotFoundEntityException을 던진다.")
  @Test
  public void getPostByPostId_ThrowsException_NotExistingPostId() {

    assertThatThrownBy(() -> boardService.getPostByPostId(-1L))
        .isInstanceOf(NotFoundEntityException.class);
  }

  @DisplayName("해당 ID의 게시물을 조회한다.")
  @Test
  public void getPostByPostId_PostReadDto_ExistingPostId() {
    PostReadDto testPostReadDto = PostReadDto.builder()
        .postId(1L)
        .build();
    when(postDao.getPostByPostId(1L)).thenReturn(Optional.ofNullable(testPostReadDto));

    PostReadDto retPostReadDto = boardService.getPostByPostId(1L);

    verify(postDao).getPostByPostId(1L);
    assertThat(retPostReadDto.getPostId()).isEqualTo(1L);
  }

  @DisplayName("게시물 상세를 조회하면 조회수가 증가한다.")
  @Test
  public void getBoardDetailByPostId_UpdateViewCount_ExistingPostId() {
    PostReadDto testPostReadDto = PostReadDto.builder()
        .postId(1L)
        .build();
    when(postDao.getPostByPostId(1L)).thenReturn(Optional.ofNullable(testPostReadDto));

    boardService.getBoardDetailByPostId(1L);

    verify(postDao).updateViewCnt(1L);
  }


  @DisplayName("게시물 상세를 조회하면 댓글도 함께 조회한다.")
  @Test
  public void getBoardDetailByBoardId_GetComments_ExistingBoardId() {
    PostReadDto testPostReadDto = PostReadDto.builder()
        .postId(1L)
        .build();
    when(postDao.getPostByPostId(1L)).thenReturn(Optional.ofNullable(testPostReadDto));

    boardService.getBoardDetailByPostId(1L);

    verify(commentDao).getCommentsByPostId(1L);
  }

  @DisplayName("게시물을 생성한다.")
  @Test
  public void createPost() {
    ArgumentCaptor<Post> createCaptor = ArgumentCaptor.forClass(Post.class);
    doNothing().when(postDao).createPost(createCaptor.capture());

    PostCreateDto createDto = PostCreateDto
        .builder()
        .title("title")
        .content("content")
        .build();
    boardService.createPost(createDto, 1L);

    verify(postDao).createPost(any(Post.class));
    assertThat(createDto.getTitle()).isEqualTo(createCaptor.getValue().getTitle());
    assertThat(createDto.getContent()).isEqualTo(createCaptor.getValue().getContent());
  }

  @DisplayName("권한이 없는 게시물을 업데이트하면 PermissionDeniedBoardException을 던진다.")
  @Test
  public void updatePost_ThrowException_NotEqualToPostUserId() {
    PostUpdateDto updateDto = PostUpdateDto.builder()
        .postId(1L)
        .userId(1L)
        .build();
    assertThatThrownBy(() -> boardService.updatePost(updateDto, 2L))
        .isInstanceOf(PermissionDeniedException.class);
  }

  @DisplayName("존재하지 않는 게시물을 업데이트하면 NotFoundBoardException을 던진다.")
  @Test
  public void updatePost_ThrowException_NotExistingPost() {
    PostUpdateDto updateDto = PostUpdateDto.builder()
        .postId(1L)
        .userId(1L)
        .build();

    when(postDao.checkPostId(updateDto.getPostId())).thenReturn(false);

    assertThatThrownBy(() -> boardService.updatePost(updateDto, 1L))
        .isInstanceOf(NotFoundEntityException.class);
  }

  @DisplayName("존재하는 게시물을 업데이트한다.")
  @Test
  public void updatePost_ExistingPost() {
    PostUpdateDto updateDto = PostUpdateDto.builder()
        .postId(1L)
        .title("title")
        .content("content")
        .userId(1L)
        .build();

    when(postDao.checkPostId(updateDto.getPostId())).thenReturn(true);
    ArgumentCaptor<Post> updateCaptor = ArgumentCaptor.forClass(Post.class);
    doNothing().when(postDao).updatePost(updateCaptor.capture());

    boardService.updatePost(updateDto, 1L);

    verify(postDao).updatePost(any(Post.class));
    assertThat(updateDto.getPostId()).isEqualTo(updateCaptor.getValue().getPostId());
    assertThat(updateDto.getTitle()).isEqualTo(updateCaptor.getValue().getTitle());
    assertThat(updateDto.getContent()).isEqualTo(updateCaptor.getValue().getContent());
  }

  @DisplayName("권한이 없는 게시물을 삭제하면 PermissionDeniedBoardException을 던진다.")
  @Test
  public void deletePost_ThrowException_NotEqualToPostUserId() {
    PostDeleteDto deleteDto = PostDeleteDto.builder()
        .userId(1L)
        .build();
    assertThatThrownBy(() -> boardService.deletePost(deleteDto, 2L))
        .isInstanceOf(PermissionDeniedException.class);
  }

  @DisplayName("존재하지 않는 게시물을 삭제하면 NotFoundBoardException을 던진다.")
  @Test
  public void deletePost_ThrowException_NotExistingPost() {
    PostDeleteDto deleteDto = PostDeleteDto.builder()
        .postId(1L)
        .userId(1L)
        .build();
    when(postDao.checkPostId(deleteDto.getPostId())).thenReturn(false);

    assertThatThrownBy(() -> boardService.deletePost(deleteDto, 1L))
        .isInstanceOf(NotFoundEntityException.class);
  }

  @DisplayName("존재하는 게시물을 삭제한다.")
  @Test
  public void deletePost_ExistingPost() {
    PostDeleteDto deleteDto = PostDeleteDto.builder()
        .postId(1L)
        .userId(1L)
        .build();
    when(postDao.checkPostId(deleteDto.getPostId())).thenReturn(true);

    boardService.deletePost(deleteDto, 1L);

    verify(postDao).deleteByPostId(deleteDto.getPostId());
  }

  @DisplayName("존재하는 게시물을 삭제 시 댓글도 삭제한다.")
  @Test
  public void deletePost_DeleteComments_ExistingPost() {
    PostDeleteDto deleteDto = PostDeleteDto.builder()
        .postId(1L)
        .userId(1L)
        .build();
    when(postDao.checkPostId(deleteDto.getPostId())).thenReturn(true);

    boardService.deletePost(deleteDto, 1L);

    verify(commentDao).deleteByPostId(deleteDto.getPostId());
  }

  @DisplayName("댓글을 생성한다.")
  @Test
  public void createComment() {
    ArgumentCaptor<Comment> createCaptor = ArgumentCaptor.forClass(Comment.class);
    doNothing().when(commentDao).createComment(createCaptor.capture());

    CommentCreateDto createDto = CommentCreateDto
        .builder()
        .content("content")
        .build();
    boardService.createComment(createDto, 1L);

    verify(commentDao).createComment(any(Comment.class));
    assertThat(createDto.getContent()).isEqualTo(createCaptor.getValue().getContent());
  }

  @DisplayName("권한이 없는 댓글을 업데이트하면 PermissionDeniedBoardException을 던진다.")
  @Test
  public void updateComment_ThrowException_NotEqualToCommentUserId() {
    CommentUpdateDto updateDto = CommentUpdateDto.builder()
        .userId(1L)
        .build();
    assertThatThrownBy(() -> boardService.updateComment(updateDto, 2L))
        .isInstanceOf(PermissionDeniedException.class);
  }

  @DisplayName("존재하지 않는 댓글을 업데이트하면 NotFoundEntityException을 던진다.")
  @Test
  public void updateComment_ThrowException_NotExistingComment() {
    CommentUpdateDto updateDto = CommentUpdateDto.builder()
        .userId(1L)
        .commentId(1L)
        .build();

    when(commentDao.checkCommentId(updateDto.getCommentId())).thenReturn(false);

    assertThatThrownBy(() -> boardService.updateComment(updateDto, 1L))
        .isInstanceOf(NotFoundEntityException.class);
  }

  @DisplayName("존재하는 댓글을 업데이트한다.")
  @Test
  public void updateComment_ExistingComment() {
    CommentUpdateDto updateDto = CommentUpdateDto.builder()
        .userId(1L)
        .content("content")
        .commentId(1L)
        .build();

    when(commentDao.checkCommentId(updateDto.getCommentId())).thenReturn(true);
    ArgumentCaptor<Comment> updateCaptor = ArgumentCaptor.forClass(Comment.class);
    doNothing().when(commentDao).updateComment(updateCaptor.capture());

    boardService.updateComment(updateDto, 1L);

    verify(commentDao).updateComment(any(Comment.class));
    assertThat(updateDto.getCommentId()).isEqualTo(updateCaptor.getValue().getCommentId());
    assertThat(updateDto.getContent()).isEqualTo(updateCaptor.getValue().getContent());
  }

  @DisplayName("권한이 없는 댓글을 삭제하면 PermissionDeniedException을 던진다.")
  @Test
  public void deleteComment_ThrowException_NotEqualToCommentUserId() {
    CommentDeleteDto deleteDto = CommentDeleteDto.builder()
        .commentId(1L)
        .userId(1L)
        .build();
    assertThatThrownBy(() -> boardService.deleteComment(deleteDto, 2L))
        .isInstanceOf(PermissionDeniedException.class);
  }

  @DisplayName("존재하지 않는 댓글을 삭제하면 NotFoundEntityException을 던진다.")
  @Test
  public void deleteByComment_ThrowException_NotExistingComment() {
    CommentDeleteDto deleteDto = CommentDeleteDto.builder()
        .commentId(1L)
        .userId(1L)
        .build();
    when(commentDao.checkCommentId(deleteDto.getCommentId())).thenReturn(false);

    assertThatThrownBy(() -> boardService.deleteComment(deleteDto, 1L))
        .isInstanceOf(NotFoundEntityException.class);
  }

  @DisplayName("존재하는 댓글을 삭제한다.")
  @Test
  public void deleteByComment_ExistingComment() {
    CommentDeleteDto deleteDto = CommentDeleteDto.builder()
        .commentId(1L)
        .userId(1L)
        .build();
    when(commentDao.checkCommentId(deleteDto.getCommentId())).thenReturn(true);

    boardService.deleteComment(deleteDto, 1L);

    verify(commentDao).deleteByCommentId(deleteDto.getCommentId());
  }
}
