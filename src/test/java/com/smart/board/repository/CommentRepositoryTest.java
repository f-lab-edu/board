package com.smart.board.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.smart.board.controller.dto.comment.CommentCreateDto;
import com.smart.board.domain.Comment;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CommentRepositoryTest {

  private CommentRepositoryImpl commentRepository = new CommentRepositoryImpl();

  private Comment comment;

  @BeforeEach
  public void create_Test_Data() {
    comment = CommentCreateDto
        .builder()
        .content("content")
        .postId(1L)
        .build()
        .toEntity(1L);
  }

  @AfterEach
  public void delete_Test_Data() {
    commentRepository.deleteByCommentId(comment.getCommentId());
  }

  @Test
  @DisplayName("댓글 생성 후 게시글 ID로 댓글 조회 시 댓글이 담긴 List를 반환한다.")
  public void findByPostId_CommentReadDtos_ExistingComments() {
    commentRepository.save(comment);

    List<Comment> comments = commentRepository.findByPostId(comment.getPostId());

    assertThat(comments.isEmpty()).isFalse();
  }

  @Test
  @DisplayName("존재하지 않는 게시글 ID로 댓글 조회 시 비어있는 List를 반환한다.")
  public void findByPostId_EmptyList_NotExistingPostId() {
    List<Comment> comment = commentRepository.findByPostId(-1L);

    assertThat(comment.isEmpty()).isTrue();
  }

  @Test
  @DisplayName("게시글에 해당하는 댓글 삭제 후 댓글이 전부 삭제되었는지 확인한다.")
  public void deleteByPostId_EmptyList_ExistingPostId() {
    commentRepository.save(comment);

    commentRepository.deleteByPostId(comment.getPostId());

    assertThat(commentRepository.findByPostId(comment.getPostId())).isEmpty();
  }

  @Test
  @DisplayName("댓글 생성 후 반환된 ID로 댓글을 조회한다.")
  public void findByCommentId_CommentReadDto_ExistingCommentId() {
    commentRepository.save(comment);

    Comment retComment = commentRepository.findByCommentId(comment.getCommentId()).get();

    assertThat(retComment).isNotNull();
    assertThat(retComment.getCommentId()).isEqualTo(comment.getCommentId());
  }

  @Test
  @DisplayName("댓글 삭제 후 해당 댓글이 삭제되었는지 확인한다.")
  public void deleteByCommentId_ExistingComment() {
    commentRepository.save(comment);

    commentRepository.deleteByCommentId(comment.getCommentId());

    assertThat(commentRepository.existsByCommentId(comment.getCommentId())).isFalse();
  }
}
