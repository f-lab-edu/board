package com.smart.board.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.smart.board.controller.dto.comment.CommentCreateDto;
import com.smart.board.controller.dto.comment.CommentReadDto;
import com.smart.board.controller.dto.comment.CommentUpdateDto;
import com.smart.board.domain.Comment;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommentDaoTest {

  @Autowired
  private CommentDao commentDao;

  Comment comment;

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
    commentDao.deleteByCommentId(comment.getCommentId());
  }

  @Test
  @DisplayName("댓글 생성 후 게시글 ID로 댓글 조회 시 댓글이 담긴 List를 반환한다.")
  public void getCommentsByPostId_CommentInfos_ExistingComments() {
    commentDao.createComment(comment);

    List<CommentReadDto> commentReadDtos = commentDao
        .getCommentsByPostId(comment.getPostId());

    assertThat(commentReadDtos.isEmpty()).isFalse();
  }

  @Test
  @DisplayName("존재하지 않는 게시글 ID로 댓글 조회 시 비어있는 List를 반환한다.")
  public void getCommentsByPostId_EmptyList_NotExistingBoardId() {
    List<CommentReadDto> commentReadDtos = commentDao.getCommentsByPostId(-1L);

    assertThat(commentReadDtos.isEmpty()).isTrue();
  }

  @Test
  @DisplayName("게시글에 해당하는 댓글 삭제 후 댓글이 전부 삭제되었는지 확인한다.")
  public void deleteByBoardId_EmptyList_ExistingBoardId() {
    commentDao.createComment(comment);

    commentDao.deleteByPostId(comment.getPostId());

    assertThat(commentDao.getCommentsByPostId(comment.getPostId())).isEmpty();
  }

  @Test
  @DisplayName("댓글 생성 후 반환된 ID로 댓글을 조회한다.")
  public void getCommentByCommentId_CommentInfo_ExistingCommentId() {
    commentDao.createComment(comment);

    CommentReadDto commentReadDto = commentDao.getCommentByCommentId(comment.getCommentId())
        .get();

    assertThat(commentReadDto).isNotNull();
    assertThat(commentReadDto.getCommentId()).isEqualTo(comment.getCommentId());
  }

  @Test
  @DisplayName("생성하지 않은 댓글을 업데이트하면 당연히 해당 댓글을 찾을 수 없다.")
  public void updateComment_NotExistingComment() {
    Comment updateComment = CommentUpdateDto.builder()
        .commentId(-1L)
        .content("update content")
        .userId(comment.getUserId())
        .build()
        .toEntity();

    commentDao.updateComment(updateComment);

    Optional<CommentReadDto> optionalCommentReadDto = commentDao.getCommentByCommentId(-1L);
    assertThat(optionalCommentReadDto.isEmpty()).isTrue();
  }

  @Test
  @DisplayName("댓글을 업데이트 후 해당하는 댓글이 정상적으로 수정되었는지 확인한다.")
  public void updateComment_ExistingComment() {
    commentDao.createComment(comment);
    Comment updateComment = CommentUpdateDto.builder()
        .commentId(comment.getCommentId())
        .content("update content")
        .userId(comment.getUserId())
        .build()
        .toEntity();

    commentDao.updateComment(updateComment);

    CommentReadDto commentReadDto = commentDao.getCommentByCommentId(comment.getCommentId())
        .get();
    assertThat(commentReadDto.getCommentId()).isEqualTo(updateComment.getCommentId());
    assertThat(commentReadDto.getContent()).isEqualTo(updateComment.getContent());
  }

  @Test
  @DisplayName("댓글 삭제 후 해당 댓글이 삭제되었는지 확인한다.")
  public void deleteByCommentId_ExistingComment() {
    commentDao.createComment(comment);

    commentDao.deleteByCommentId(comment.getCommentId());

    assertThat(commentDao.checkCommentId(comment.getCommentId())).isFalse();
  }
}
