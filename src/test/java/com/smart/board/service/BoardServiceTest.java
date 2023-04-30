package com.smart.board.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;

import com.smart.board.controller.dto.comment.CommentCreateDto;
import com.smart.board.controller.dto.comment.CommentUpdateDto;
import com.smart.board.controller.dto.post.PostCreateDto;
import com.smart.board.controller.dto.post.PostReadDto;
import com.smart.board.controller.dto.post.PostUpdateDto;
import com.smart.board.repository.CommentRepository;
import com.smart.board.repository.CommentRepositoryImpl;
import com.smart.board.repository.PostRepository;
import com.smart.board.repository.PostRepositoryImpl;
import com.smart.global.error.NotFoundEntityException;
import com.smart.global.error.PermissionDeniedException;
import com.smart.user.domain.User;
import com.smart.user.repository.UserRepository;
import com.smart.user.repository.UserRepositoryImpl;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BoardServiceTest {

  private PostRepository postRepository = new PostRepositoryImpl();

  private CommentRepository commentRepository = new CommentRepositoryImpl();

  private UserRepository userRepository = new UserRepositoryImpl();

  private BoardService boardService = new BoardService(postRepository, commentRepository,
      userRepository);

  private Long userId;

  @BeforeEach
  public void create_Test_Data() {
    User user = User.builder()
        .nickname("nickname")
        .build();
    userId = userRepository.save(user);
  }

  @DisplayName("게시물 없다면 비어있는 게시판 목록을 반환한다.")
  @Test
  public void getPosts_EmptyList_NotExistingPost() {
    List<PostReadDto> posts = boardService.getPosts();

    assertThat(posts.isEmpty()).isTrue();
  }

  @DisplayName("게시판 목록을 반환한다.")
  @Test
  public void getPosts_PostList_ExistingPost() {
    PostCreateDto createDto = PostCreateDto
        .builder()
        .title("title")
        .content("content")
        .build();
    Long postId = boardService.createPost(createDto, userId);

    List<PostReadDto> retPosts = boardService.getPosts();

    assertThat(retPosts.size()).isEqualTo(1);
    assertThat(retPosts.get(0).getPostId()).isEqualTo(postId);
    assertThat(retPosts.get(0).getUserId()).isEqualTo(userId);
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
    PostCreateDto createDto = PostCreateDto
        .builder()
        .title("title")
        .content("content")
        .build();
    Long postId = boardService.createPost(createDto, userId);

    PostReadDto retPostReadDto = boardService.getPostByPostId(postId);

    assertThat(retPostReadDto.getPostId()).isEqualTo(postId);
    assertThat(retPostReadDto.getUserId()).isEqualTo(userId);
  }

  @DisplayName("해당 게시물의 조회수를 증가시킨다.")
  @Test
  public void getBoardDetailByPostId_UpdateViewCount_ExistingPostId() {
    PostCreateDto createDto = PostCreateDto
        .builder()
        .title("title")
        .content("content")
        .build();
    Long postId = boardService.createPost(createDto, userId);

    boardService.updatePostViewCount(postId);

    assertThat(boardService.getPostByPostId(postId).getViewCount()).isEqualTo(1L);
  }

  @DisplayName("존재하지 않는 게시물을 업데이트하면 NotFoundEntityException을 던진다.")
  @Test
  public void updatePost_ThrowException_NotExistingPost() {
    PostUpdateDto updateDto = PostUpdateDto.builder()
        .postId(1L)
        .build();

    assertThatThrownBy(() -> boardService.updatePost(updateDto, 1L))
        .isInstanceOf(NotFoundEntityException.class);
  }

  @DisplayName("권한이 없는 게시물을 업데이트하면 PermissionDeniedBoardException을 던진다.")
  @Test
  public void updatePost_ThrowException_NotEqualToPostUserId() {
    PostCreateDto createDto = PostCreateDto
        .builder()
        .title("title")
        .content("content")
        .build();
    Long postId = boardService.createPost(createDto, userId);
    PostUpdateDto updateDto = PostUpdateDto.builder()
        .postId(postId)
        .build();

    assertThatThrownBy(() -> boardService.updatePost(updateDto, 2L))
        .isInstanceOf(PermissionDeniedException.class);
  }

  @DisplayName("존재하는 게시물을 업데이트한다.")
  @Test
  public void updatePost_ExistingPost() {
    PostCreateDto createDto = PostCreateDto
        .builder()
        .title("title")
        .content("content")
        .build();
    Long postId = boardService.createPost(createDto, userId);
    PostUpdateDto updateDto = PostUpdateDto.builder()
        .postId(postId)
        .title("update title")
        .content("update content")
        .build();

    boardService.updatePost(updateDto, userId);

    assertThat(boardService.getPostByPostId(postId).getTitle()).isEqualTo(updateDto.getTitle());
    assertThat(boardService.getPostByPostId(postId).getContent()).isEqualTo(updateDto.getContent());
  }

  @DisplayName("존재하지 않는 게시물을 삭제하면 NotFoundEntityException을 던진다.")
  @Test
  public void deletePost_ThrowException_NotExistingPost() {
    assertThatThrownBy(() -> boardService.deletePost(-1L, userId))
        .isInstanceOf(NotFoundEntityException.class);
  }

  @DisplayName("권한이 없는 게시물을 삭제하면 PermissionDeniedBoardException을 던진다.")
  @Test
  public void deletePost_ThrowException_NotEqualToPostUserId() {
    PostCreateDto createDto = PostCreateDto
        .builder()
        .title("title")
        .content("content")
        .build();
    Long postId = boardService.createPost(createDto, userId);

    assertThatThrownBy(() -> boardService.deletePost(postId, 2L))
        .isInstanceOf(PermissionDeniedException.class);
  }

  @DisplayName("존재하는 게시물을 삭제한다.")
  @Test
  public void deletePost_ExistingPost() {
    PostCreateDto createDto = PostCreateDto
        .builder()
        .title("title")
        .content("content")
        .build();
    Long postId = boardService.createPost(createDto, userId);

    boardService.deletePost(postId, userId);

    assertThatThrownBy(() -> boardService.getPostByPostId(postId))
        .isInstanceOf(NotFoundEntityException.class);
  }

  @DisplayName("존재하는 게시물을 삭제 시 댓글도 삭제한다.")
  @Test
  public void deletePost_DeleteComments_ExistingPost() {
    PostCreateDto createDto = PostCreateDto
        .builder()
        .title("title")
        .content("content")
        .build();
    Long postId = boardService.createPost(createDto, userId);

    boardService.deletePost(postId, userId);

    assertThat(boardService.getCommentsByPostId(postId).isEmpty()).isTrue();
  }

  @DisplayName("존재하지 않는 댓글을 업데이트하면 NotFoundEntityException을 던진다.")
  @Test
  public void updateComment_ThrowException_NotExistingComment() {
    CommentUpdateDto updateDto = CommentUpdateDto.builder()
        .commentId(-1L)
        .build();

    assertThatThrownBy(() -> boardService.updateComment(updateDto, userId))
        .isInstanceOf(NotFoundEntityException.class);
  }

  @DisplayName("권한이 없는 댓글을 업데이트하면 PermissionDeniedBoardException을 던진다.")
  @Test
  public void updateComment_ThrowException_NotEqualToCommentUserId() {
    CommentCreateDto createDto = CommentCreateDto.builder()
        .content("content")
        .build();
    Long commentId = boardService.createComment(createDto, userId);
    CommentUpdateDto updateDto = CommentUpdateDto.builder()
        .commentId(commentId)
        .build();

    assertThatThrownBy(() -> boardService.updateComment(updateDto, 2L))
        .isInstanceOf(PermissionDeniedException.class);
  }

  @DisplayName("존재하는 댓글을 업데이트한다.")
  @Test
  public void updateComment_ExistingComment() {
    CommentCreateDto createDto = CommentCreateDto.builder()
        .content("content")
        .build();
    Long commentId = boardService.createComment(createDto, userId);
    CommentUpdateDto updateDto = CommentUpdateDto.builder()
        .content("update content")
        .commentId(commentId)
        .build();

    boardService.updateComment(updateDto, userId);

    assertThat(boardService.getCommentsByCommentId(commentId).getContent())
        .isEqualTo(updateDto.getContent());
  }

  @DisplayName("존재하지 않는 댓글을 삭제하면 NotFoundEntityException을 던진다.")
  @Test
  public void deleteByComment_ThrowException_NotExistingComment() {

    assertThatThrownBy(() -> boardService.deleteComment(-1L, userId))
        .isInstanceOf(NotFoundEntityException.class);
  }

  @DisplayName("권한이 없는 댓글을 삭제하면 PermissionDeniedException을 던진다.")
  @Test
  public void deleteComment_ThrowException_NotEqualToCommentUserId() {
    CommentCreateDto createDto = CommentCreateDto.builder()
        .content("content")
        .build();
    Long commentId = boardService.createComment(createDto, userId);

    assertThatThrownBy(() -> boardService.deleteComment(commentId, 2L))
        .isInstanceOf(PermissionDeniedException.class);
  }

  @DisplayName("존재하는 댓글을 삭제한다.")
  @Test
  public void deleteByComment_ExistingComment() {
    CommentCreateDto createDto = CommentCreateDto.builder()
        .content("content")
        .build();
    Long commentId = boardService.createComment(createDto, userId);

    boardService.deleteComment(commentId, userId);

    assertThatThrownBy(() -> boardService.getCommentsByCommentId(commentId))
        .isInstanceOf(NotFoundEntityException.class);
  }
}
