package com.smart.comment.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.smart.comment.controller.dto.CommentDto;
import com.smart.comment.controller.dto.CommentDto.CommentInfo;
import com.smart.comment.domain.Comment;
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
  public void 테스트댓글생성() {
    comment = CommentDto.CreateRequest
        .builder()
        .content("content")
        .boardId(1L)
        .build()
        .toEntity(1L);
  }

  @AfterEach
  public void 테스트댓글삭제() {
    commentDao.deleteByCommentId(comment.getBoardId());
  }

  @Test
  @DisplayName("댓글 생성 후 게시판 ID로 댓글 조회 시 댓글이 담긴 List를 반환한다.")
  public void getCommentsByBoardId_CommentInfos_ExistingComments() {
    commentDao.createComment(comment);

    List<CommentInfo> commentInfos = commentDao.getCommentsByBoardId(comment.getBoardId());

    assertThat(commentInfos.isEmpty()).isFalse();
  }

  @Test
  @DisplayName("존재하지 않는 게시판 ID로 댓글 조회 시 비어있는 List를 반환한다.")
  public void getCommentsByBoardId_EmptyList_NotExistingBoardId() {
    List<CommentInfo> commentInfos = commentDao.getCommentsByBoardId(-1L);

    assertThat(commentInfos.isEmpty()).isTrue();
  }

  @Test
  @DisplayName("게시글에 해당하는 댓글 삭제 후 댓글이 전부 삭제되었는지 확인한다.")
  public void deleteByBoardId_EmptyList_ExistingBoardId() {
    commentDao.createComment(comment);

    commentDao.deleteByBoardId(comment.getBoardId());

    assertThat(commentDao.getCommentsByBoardId(comment.getBoardId())).isEmpty();
  }

  @Test
  @DisplayName("댓글 생성 후 반환된 ID로 댓글을 조회한다.")
  public void getCommentByCommentId_CommentInfo_ExistingCommentId() {
    commentDao.createComment(comment);

    CommentInfo commentInfo = commentDao.getCommentByCommentId(comment.getCommentId()).get();

    assertThat(commentInfo).isNotNull();
    assertThat(commentInfo.getCommentId()).isEqualTo(comment.getCommentId());
  }

  @Test
  @DisplayName("생성하지 않은 댓글을 업데이트하면 당연히 해당 댓글을 찾을 수 없다.")
  public void updateComment_NotExistingComment() {
    Comment updateComment = CommentDto.UpdateRequest.builder()
        .commentId(-1L)
        .content("update content")
        .userId(comment.getUserId())
        .build()
        .toEntity();

    commentDao.updateComment(updateComment);

    Optional<CommentInfo> optionalCommentInfo = commentDao.getCommentByCommentId(-1L);
    assertThat(optionalCommentInfo.isEmpty()).isTrue();
  }

  @Test
  @DisplayName("댓글을 업데이트 후 해당하는 댓글이 정상적으로 수정되었는지 확인한다.")
  public void updateComment_ExistingComment() {
    commentDao.createComment(comment);
    Comment updateComment = CommentDto.UpdateRequest.builder()
        .commentId(comment.getCommentId())
        .content("update content")
        .userId(comment.getUserId())
        .build()
        .toEntity();

    commentDao.updateComment(updateComment);

    CommentInfo commentInfo = commentDao.getCommentByCommentId(comment.getCommentId()).get();
    assertThat(commentInfo.getCommentId()).isEqualTo(updateComment.getCommentId());
    assertThat(commentInfo.getContent()).isEqualTo(updateComment.getContent());
  }

  @Test
  @DisplayName("댓글 삭제 후 해당 댓글이 삭제되었는지 확인한다.")
  public void deleteByCommentId_ExistingComment() {
    commentDao.createComment(comment);

    commentDao.deleteByCommentId(comment.getCommentId());

    assertThat(commentDao.checkCommentId(comment.getCommentId())).isFalse();
  }
}
