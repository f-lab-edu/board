package com.smart.board.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.smart.board.controller.dto.BoardDto.BoardInfo;
import com.smart.board.controller.dto.BoardDto.CreateRequest;
import com.smart.board.controller.dto.BoardDto.DeleteRequest;
import com.smart.board.controller.dto.BoardDto.UpdateRequest;
import com.smart.board.dao.BoardDao;
import com.smart.board.domain.Board;
import com.smart.comment.dao.CommentDao;
import com.smart.global.error.NotFoundBoardException;
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

  BoardDao boardDao;

  CommentDao commentDao;

  BoardService boardService;

  BoardInfo boardInfo;

  UpdateRequest updateRequest;

  DeleteRequest deleteRequest;

  @BeforeEach
  public void inject_Mock() {
    boardDao = mock(BoardDao.class);
    commentDao = mock(CommentDao.class);
    boardService = new BoardService(boardDao, commentDao);
  }

  @BeforeEach
  public void create_Test_Data() {
    boardInfo = BoardInfo.builder()
        .boardId(1L)
        .title("title")
        .content("content")
        .viewCount(1L)
        .userId(1L)
        .nickname("nickname")
        .build();

    updateRequest = UpdateRequest
        .builder()
        .boardId(1L)
        .title("update title")
        .content("update content")
        .userId(1L)
        .build();

    deleteRequest = DeleteRequest
        .builder()
        .boardId(1L)
        .userId(1L)
        .build();
  }

  @DisplayName("게시물 없다면 비어있는 게시물 리스트를 반환한다.")
  @Test
  public void getAllBoard_EmptyList_NotExistingBoard() {
    when(boardDao.getAllBoard()).thenReturn(new ArrayList<>());

    List<BoardInfo> boardLists = boardService.getAllBoard();

    verify(boardDao).getAllBoard();
    assertThat(boardLists.isEmpty()).isTrue();
  }

  @DisplayName("모든 게시물들을 반환한다.")
  @Test
  public void getAllBoard_BoardInfos_ExistingBoard() {
    List<BoardInfo> boardInfos = new ArrayList<>();
    boardInfos.add(boardInfo);
    when(boardDao.getAllBoard()).thenReturn(boardInfos);

    List<BoardInfo> retBoardLists = boardService.getAllBoard();

    verify(boardDao).getAllBoard();
    assertThat(boardInfos.size()).isEqualTo(retBoardLists.size());
    assertThat(boardInfo).isIn(retBoardLists);
  }

  @DisplayName("해당 ID의 게시물이 없다면 NotFoundBoardException을 던진다.")
  @Test
  public void getBoardByBoardId_ThrowsException_NotExistingBoardId() {

    assertThatThrownBy(() -> boardService.getBoardByBoardId(1L))
        .isInstanceOf(NotFoundBoardException.class);
  }

  @DisplayName("해당 ID의 게시물을 조회한다.")
  @Test
  public void getBoardByBoardId_BoardInfo_ExistingBoardId() {
    when(boardDao.getBoardByBoardId(1L)).thenReturn(Optional.ofNullable(boardInfo));

    BoardInfo boardInfo = boardService.getBoardByBoardId(1L);

    verify(boardDao).getBoardByBoardId(1L);
    assertThat(1L).isEqualTo(boardInfo.getBoardId());
  }

  @DisplayName("게시물 상세를 조회하면 조회수가 증가한다.")
  @Test
  public void getBoardDetailByBoardId_UpdateViewCount_ExistingBoardId() {
    ArgumentCaptor<Long> updateViewCountCaptor = ArgumentCaptor.forClass(Long.class);
    doNothing().when(boardDao).updateViewCnt(updateViewCountCaptor.capture());
    when(boardDao.getBoardByBoardId(1L)).thenReturn(Optional.ofNullable(boardInfo));

    boardService.getBoardDetailByBoardId(1L);

    verify(boardDao).updateViewCnt(any(Long.class));
    assertThat(1L).isEqualTo(updateViewCountCaptor.getValue());
  }

  @DisplayName("게시물 상세를 조회하면 댓글도 함께 조회한다.")
  @Test
  public void getBoardDetailByBoardId_GetComments_ExistingBoardId() {
    when(boardDao.getBoardByBoardId(1L)).thenReturn(Optional.ofNullable(boardInfo));

    boardService.getBoardDetailByBoardId(1L);

    verify(commentDao).getCommentsByBoardId(1L);
  }

  @DisplayName("게시물을 생성한다.")
  @Test
  public void createBoard() {
    ArgumentCaptor<Board> createCaptor = ArgumentCaptor.forClass(Board.class);
    doNothing().when(boardDao).createBoard(createCaptor.capture());

    CreateRequest createRequest = CreateRequest
        .builder()
        .title("title")
        .content("content")
        .build();
    boardService.createBoard(createRequest, 1L);

    verify(boardDao).createBoard(any(Board.class));
    assertThat(createRequest.getTitle()).isEqualTo(createCaptor.getValue().getTitle());
    assertThat(createRequest.getContent()).isEqualTo(createCaptor.getValue().getContent());
  }

  @DisplayName("권한이 없는 게시물을 업데이트하면 PermissionDeniedBoardException을 던진다.")
  @Test
  public void updateBoard_ThrowException_NotEqualToBoardUserId() {
    assertThatThrownBy(() -> boardService.updateBoard(updateRequest, 2L))
        .isInstanceOf(PermissionDeniedException.class);
  }

  @DisplayName("존재하지 않는 게시물을 업데이트하면 NotFoundBoardException을 던진다.")
  @Test
  public void updateBoard_ThrowException_NotExistingBoard() {
    when(boardDao.checkBoardId(1L)).thenReturn(false);

    assertThatThrownBy(() -> boardService.updateBoard(updateRequest, 1L))
        .isInstanceOf(NotFoundBoardException.class);
  }

  @DisplayName("존재하는 게시물을 업데이트한다.")
  @Test
  public void updateBoard_ExistingBoard() {
    when(boardDao.checkBoardId(1L)).thenReturn(true);
    ArgumentCaptor<Board> updateCaptor = ArgumentCaptor.forClass(Board.class);
    doNothing().when(boardDao).updateBoard(updateCaptor.capture());

    Long boardId = boardService.updateBoard(updateRequest, 1L);

    verify(boardDao).updateBoard(any(Board.class));
    assertThat(boardId).isEqualTo(updateCaptor.getValue().getBoardId());
    assertThat(updateRequest.getTitle()).isEqualTo(updateCaptor.getValue().getTitle());
    assertThat(updateRequest.getContent()).isEqualTo(updateCaptor.getValue().getContent());
  }

  @DisplayName("권한이 없는 게시물을 삭제하면 PermissionDeniedBoardException을 던진다.")
  @Test
  public void deleteByBoardId_ThrowException_NotEqualToBoardUserId() {
    assertThatThrownBy(() -> boardService.deleteByBoardId(deleteRequest, 2L))
        .isInstanceOf(PermissionDeniedException.class);
  }

  @DisplayName("존재하지 않는 게시물을 삭제하면 NotFoundBoardException을 던진다.")
  @Test
  public void deleteByBoardId_ThrowException_NotExistingBoard() {
    when(boardDao.checkBoardId(1L)).thenReturn(false);

    assertThatThrownBy(() -> boardService.deleteByBoardId(deleteRequest, 1L))
        .isInstanceOf(NotFoundBoardException.class);
  }

  @DisplayName("존재하는 게시물을 삭제한다.")
  @Test
  public void deleteByBoardId_ExistingBoard() {
    when(boardDao.checkBoardId(1L)).thenReturn(true);
    ArgumentCaptor<Long> deleteCaptor = ArgumentCaptor.forClass(Long.class);
    doNothing().when(boardDao).deleteByBoardId(deleteCaptor.capture());

    boardService.deleteByBoardId(deleteRequest, 1L);

    verify(boardDao).deleteByBoardId(any(Long.class));
    assertThat(deleteRequest.getBoardId()).isEqualTo(deleteCaptor.getValue());
  }

  @DisplayName("존재하는 게시물을 삭제 시 댓글도 삭제한다.")
  @Test
  public void deleteByBoardId_DeleteComments_ExistingBoard() {
    when(boardDao.checkBoardId(1L)).thenReturn(true);
    ArgumentCaptor<Long> deleteCaptor = ArgumentCaptor.forClass(Long.class);
    doNothing().when(commentDao).deleteByBoardId(deleteCaptor.capture());

    boardService.deleteByBoardId(deleteRequest, 1L);

    verify(commentDao).deleteByBoardId(any(Long.class));
    assertThat(deleteRequest.getBoardId()).isEqualTo(deleteCaptor.getValue());
  }
}
