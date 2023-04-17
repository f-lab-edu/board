package com.smart.board.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.smart.board.controller.dto.BoardDto;
import com.smart.board.domain.Board;
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
public class BoardDaoTest {

  @Autowired
  private BoardDao boardDao;

  Board board;

  @BeforeEach
  public void create_Test_Data() {
    board = BoardDto.CreateRequest
        .builder()
        .title("title")
        .content("content")
        .build()
        .toEntity(1L);
  }

  @AfterEach
  public void delete_Test_Data() {
    boardDao.deleteByBoardId(board.getBoardId());
  }

  @Test
  @DisplayName("게시물 생성 후 게시글 전체 조회 시 게시물이 담긴 List를 반환한다.")
  public void getAllBoard_BoardInfos_ExistingBoard() {
    boardDao.createBoard(board);

    List<BoardDto.BoardInfo> boardInfos = boardDao.getAllBoard();

    assertThat(boardInfos.isEmpty()).isFalse();
  }

  @Test
  @DisplayName("존재하지 않는 ID로 게시글 조회 시 null을 반환한다.")
  public void getBoardByBoardId_Null_NotExistingBoardId() {
    Optional<BoardDto.BoardInfo> optionalBoardInfo = boardDao.getBoardByBoardId(-1L);

    assertThat(optionalBoardInfo.isEmpty()).isTrue();
  }

  @Test
  @DisplayName("게시물 생성 후 반환된 ID로 게시물을 조회한다.")
  public void getBoardByBoardId_BoardInfo_ExistingBoardId() {
    boardDao.createBoard(board);

    BoardDto.BoardInfo boardInfo = boardDao.getBoardByBoardId(board.getBoardId()).get();

    assertThat(boardInfo).isNotNull();
    assertThat(boardInfo.getBoardId()).isEqualTo(board.getBoardId());
  }

  @Test
  @DisplayName("존재하지 않는 게시물 조회수를 업데이트 한다.")
  public void updateViewCnt_NotExistingBoardId() {
    boardDao.updateViewCnt(-1L);

    Optional<BoardDto.BoardInfo> optionalBoardInfo = boardDao.getBoardByBoardId(-1L);

    assertThat(optionalBoardInfo.isEmpty()).isTrue();
  }

  @Test
  @DisplayName("게시물 생성 후 반환된 ID로 게시물 조회수를 업데이트 한다.")
  public void updateViewCnt_ExistingBoardId() {
    boardDao.createBoard(board);

    boardDao.updateViewCnt(board.getBoardId());
    BoardDto.BoardInfo boardInfo = boardDao.getBoardByBoardId(board.getBoardId()).get();

    assertThat(boardInfo.getViewCount()).isEqualTo(board.getViewCount()+1);
  }

  @Test
  @DisplayName("생성하지 않은 게시글 업데이트하면 당연히 해당 게시물을 찾을 수 없다.")
  public void updateBoard_NotExistingBoard() {
    Board updateBoard = BoardDto.UpdateRequest.builder()
        .boardId(-1L)
        .title("update title")
        .content("update content")
        .userId(board.getUserId())
        .build()
        .toEntity();

    boardDao.updateBoard(updateBoard);

    Optional<BoardDto.BoardInfo> optionalBoardInfo = boardDao.getBoardByBoardId(-1L);
    assertThat(optionalBoardInfo.isEmpty()).isTrue();
  }

  @Test
  @DisplayName("게시글 업데이트 후 해당하는 게시글이 정상적으로 수정되었는지 확인한다.")
  public void updateBoard_ExistingBoard() {
    boardDao.createBoard(board);
    Board updateBoard = BoardDto.UpdateRequest.builder()
        .boardId(board.getBoardId())
        .title("update title")
        .content("update content")
        .userId(board.getUserId())
        .build()
        .toEntity();

    boardDao.updateBoard(updateBoard);

    BoardDto.BoardInfo boardInfo = boardDao.getBoardByBoardId(board.getBoardId()).get();
    assertThat(boardInfo.getBoardId()).isEqualTo(updateBoard.getBoardId());
    assertThat(boardInfo.getTitle()).isEqualTo(updateBoard.getTitle());
    assertThat(boardInfo.getContent()).isEqualTo(updateBoard.getContent());
  }

  @Test
  @DisplayName("게시글 삭제 후 해당 게시글이 삭제되었는지 확인한다.")
  public void deleteByBoardId_ExistingBoardId() {
    boardDao.createBoard(board);

    boardDao.deleteByBoardId(board.getBoardId());

    assertThat(boardDao.checkBoardId(board.getBoardId())).isFalse();
  }
}
