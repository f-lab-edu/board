package com.smart.board.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.smart.board.controller.dto.BoardDto;
import com.smart.board.controller.dto.BoardDto.UpdateRequest;
import com.smart.board.domain.Board;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BoardDaoTest {

  @Autowired
  private BoardDao boardDao;

  Board board;

  @BeforeEach
  public void 테스트게시물생성() {
    board = BoardDto.CreateRequest
        .builder()
        .title("title")
        .content("content")
        .build()
        .toEntity(1L);
  }

  @AfterEach
  public void 테스트게시물삭제() {
    boardDao.deleteByBoardId(board.getBoardId());
  }

  @Test
  @DisplayName("게시글 생성 후 반환된 ID에 해당하는 게시글이 정상적으로 생성 되었는지 확인한다.")
  public void 게시판게시글생성() {
    boardDao.createBoard(board);

    assertThat(boardDao.checkBoardId(board.getBoardId())).isTrue();
  }

  @Test
  @DisplayName("게시글 생성 및 업데이트 후 해당하는 게시글을 찾아 정상적으로 업데이트 되었는지 확인한다.")
  public void 게시판게시글수정() {
    boardDao.createBoard(board);
    Board updateBoard = UpdateRequest.builder()
        .boardId(board.getBoardId())
        .title("update title")
        .content("update content")
        .userId(board.getUserId())
        .build()
        .toEntity();

    boardDao.updateBoard(updateBoard);

    BoardDto.Response retBoard = boardDao.getBoardByBoardId(board.getBoardId());
    assertThat(retBoard.getBoardId()).isEqualTo(updateBoard.getBoardId());
    assertThat(retBoard.getTitle()).isEqualTo(updateBoard.getTitle());
    assertThat(retBoard.getContent()).isEqualTo(updateBoard.getContent());
  }

  @Test
  @DisplayName("게시글 생성 및 삭제 후 해당하는 게시글이 정상적으로 삭제되었는지 확인한다.")
  public void 게시판게시글삭제() {
    boardDao.createBoard(board);

    boardDao.deleteByBoardId(board.getBoardId());

    assertThat(boardDao.checkBoardId(board.getBoardId())).isFalse();
  }
}
