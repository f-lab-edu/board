package com.smart.board.service;

import com.smart.board.controller.dto.BoardDto.BoardInfo;
import com.smart.board.controller.dto.BoardDto.CreateRequest;
import com.smart.board.controller.dto.BoardDto.DeleteRequest;
import com.smart.board.controller.dto.BoardDto.UpdateRequest;
import com.smart.board.dao.BoardDao;
import com.smart.board.domain.Board;
import com.smart.global.error.NotFoundBoardException;
import com.smart.global.error.PermissionDeniedBoardException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

  private final BoardDao boardDao;

  public BoardService(BoardDao boardDao) {
    this.boardDao = boardDao;
  }

  public List<BoardInfo> getAllBoard() {
    return boardDao.getAllBoard();
  }

  public BoardInfo getBoardByBoardId(Long boardId) {
    boardDao.updateViewCnt(boardId);
    BoardInfo boardInfo = boardDao.getBoardByBoardId(boardId).orElseThrow(() -> {
      throw new NotFoundBoardException();
    });
    return boardInfo;
  }

  public Long createBoard(CreateRequest request, Long loginUserId) {
    Board board = request.toEntity(loginUserId);
    boardDao.createBoard(board);
    return board.getBoardId();
  }

  public Long updateBoard(UpdateRequest request, Long loginUserId) {
    checkPermission(loginUserId, request.getUserId());
    checkExistBoard(request.getBoardId());
    boardDao.updateBoard(request.toEntity());
    return request.getBoardId();
  }

  public void deleteByBoardId(DeleteRequest request, Long loginUserId) {
    checkPermission(loginUserId, request.getUserId());
    checkExistBoard(request.getBoardId());
    boardDao.deleteByBoardId(request.getBoardId());
  }

  private void checkExistBoard(Long boardId) {
    if (!boardDao.checkBoardId(boardId)) {
      throw new NotFoundBoardException();
    }
  }

  private void checkPermission(Long loginUserId, Long authorUserId) {
    if (loginUserId != authorUserId) {
      throw new PermissionDeniedBoardException();
    }
  }
}
