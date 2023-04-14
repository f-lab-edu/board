package com.smart.board.service;

import com.smart.board.controller.dto.BoardDto.CreateRequest;
import com.smart.board.controller.dto.BoardDto.DeleteRequest;
import com.smart.board.controller.dto.BoardDto.Response;
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

  public List<Response> getAllBoard() {
    return boardDao.getAllBoard();
  }

  public Response getBoardByBoardId(Long boardId) {
    checkExistBoard(boardId);
    return boardDao.getBoardByBoardId(boardId);
  }

  public Long createBoard(CreateRequest request, Long userId) {
    Board board = request.toEntity(userId);
    boardDao.createBoard(board);
    return board.getBoardId();
  }

  public Long updateBoard(UpdateRequest request, Long userId) {
    checkPermission(userId, request.getUserId());
    checkExistBoard(request.getBoardId());
    boardDao.updateBoard(request.toEntity());
    return request.getBoardId();
  }

  public void deleteByBoardId(DeleteRequest request, Long userId) {
    checkPermission(userId, request.getUserId());
    checkExistBoard(request.getBoardId());
    boardDao.deleteByBoardId(request.getBoardId());
  }

  private void checkExistBoard(Long boardId) {
    if (!boardDao.checkBoardId(boardId)) {
      throw new NotFoundBoardException();
    }
  }

  private void checkPermission(Long userId, Long userId2) {
    if (userId2 != userId) {
      throw new PermissionDeniedBoardException();
    }
  }
}
