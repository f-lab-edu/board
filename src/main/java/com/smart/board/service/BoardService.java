package com.smart.board.service;

import com.smart.board.controller.dto.BoardDto.BoardDetail;
import com.smart.board.controller.dto.BoardDto.BoardInfo;
import com.smart.board.controller.dto.BoardDto.CreateRequest;
import com.smart.board.controller.dto.BoardDto.DeleteRequest;
import com.smart.board.controller.dto.BoardDto.UpdateRequest;
import com.smart.board.dao.BoardDao;
import com.smart.board.domain.Board;
import com.smart.comment.controller.dto.CommentDto.CommentInfo;
import com.smart.comment.dao.CommentDao;
import com.smart.global.error.NotFoundBoardException;
import com.smart.global.error.PermissionDeniedException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardService {

  private final BoardDao boardDao;
  private final CommentDao commentDao;

  public BoardService(BoardDao boardDao, CommentDao commentDao) {
    this.boardDao = boardDao;
    this.commentDao = commentDao;
  }

  public List<BoardInfo> getAllBoard() {
    return boardDao.getAllBoard();
  }

  public BoardInfo getBoardByBoardId(Long boardId) {
    BoardInfo boardInfo = boardDao.getBoardByBoardId(boardId).orElseThrow(() -> {
      throw new NotFoundBoardException();
    });
    return boardInfo;
  }

  public BoardDetail getBoardDetailByBoardId(Long boardId) {
    boardDao.updateViewCnt(boardId);
    BoardInfo boardInfo = boardDao.getBoardByBoardId(boardId).orElseThrow(() -> {
      throw new NotFoundBoardException();
    });
    List<CommentInfo> commentInfos = commentDao.getCommentsByBoardId(boardId);
    return BoardDetail.builder()
        .boardInfo(boardInfo)
        .commentInfos(commentInfos)
        .build();
  }

  @Transactional
  public Long createBoard(CreateRequest request, Long loginUserId) {
    Board board = request.toEntity(loginUserId);
    boardDao.createBoard(board);
    return board.getBoardId();
  }

  @Transactional
  public Long updateBoard(UpdateRequest request, Long loginUserId) {
    checkPermission(loginUserId, request.getUserId());
    checkExistBoard(request.getBoardId());
    boardDao.updateBoard(request.toEntity());
    return request.getBoardId();
  }

  @Transactional
  public void deleteByBoardId(DeleteRequest request, Long loginUserId) {
    checkPermission(loginUserId, request.getUserId());
    checkExistBoard(request.getBoardId());
    commentDao.deleteByBoardId(request.getBoardId());
    boardDao.deleteByBoardId(request.getBoardId());
  }

  private void checkExistBoard(Long boardId) {
    if (!boardDao.checkBoardId(boardId)) {
      throw new NotFoundBoardException();
    }
  }

  private void checkPermission(Long loginUserId, Long authorUserId) {
    if (!loginUserId.equals(authorUserId)) {
      throw new PermissionDeniedException();
    }
  }
}
