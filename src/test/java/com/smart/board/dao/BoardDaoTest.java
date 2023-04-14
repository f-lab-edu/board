package com.smart.board.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.smart.board.controller.dto.BoardDto;
import com.smart.board.controller.dto.BoardDto.Response;
import com.smart.board.controller.dto.BoardDto.UpdateRequest;
import com.smart.board.domain.Board;
import java.util.List;
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
  @DisplayName("게시물이 없다면 게시판 전체 조회 시 비어있는 List를 반환한다.")
  public void getAllBoard_EmptyList_NotExistingBoard(){
    List<Response> allBoard = boardDao.getAllBoard();

    assertThat(allBoard.isEmpty()).isTrue();
  }

  @Test
  @DisplayName("게시물 생성 후 게시판 전체 조회 시 게시물이 담긴 List를 반환한다.")
  public void getAllBoard_ResponseList_ExistingBoard(){
    boardDao.createBoard(board);

    List<Response> allBoard = boardDao.getAllBoard();

    assertThat(allBoard.isEmpty()).isFalse();
    assertThat(allBoard.get(0).getBoardId()).isEqualTo(board.getBoardId());
  }

  @Test
  @DisplayName("존재하지 않는 ID로 게시판 조회 시 null을 반환한다.")
  public void getBoardByBoardId_Null_NotExistingBoardId(){
    Response response = boardDao.getBoardByBoardId(-1L);

    assertThat(response).isNull();
  }

  @Test
  @DisplayName("게시물 생성 후 반환된 ID로 게시물 조회 시 게시물을 반환한다.")
  public void getBoardByBoardId_Response_ExistingBoardId(){
    boardDao.createBoard(board);

    Response response = boardDao.getBoardByBoardId(board.getBoardId());

    assertThat(response).isNotNull();
    assertThat(response.getBoardId()).isEqualTo(board.getBoardId());
  }

  @Test
  @DisplayName("생성하지 않은 게시글 업데이트하면 당연히 해당 게시물을 찾을 수 없다.")
  public void updateBoard_NotExistingBoard() {
    Board updateBoard = UpdateRequest.builder()
        .boardId(-1L)
        .title("update title")
        .content("update content")
        .userId(board.getUserId())
        .build()
        .toEntity();

    boardDao.updateBoard(updateBoard);

    Response response = boardDao.getBoardByBoardId(-1L);
    assertThat(response).isNull();
  }

  @Test
  @DisplayName("게시글 업데이트 후 해당하는 게시글이 정상적으로 수정되었는지 확인한다.")
  public void updateBoard_ExistingBoard() {
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
  @DisplayName("게시글 삭제 후 해당 게시글이 삭제되었는지 확인한다.")
  public void deleteByBoardId_ExistingBoardId() {
    boardDao.createBoard(board);

    boardDao.deleteByBoardId(board.getBoardId());

    assertThat(boardDao.checkBoardId(board.getBoardId())).isFalse();
  }
}
