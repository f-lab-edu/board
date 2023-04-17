package com.smart.board.service;

import com.smart.board.controller.dto.BoardDto;
import com.smart.board.dao.BoardDao;
import com.smart.board.domain.Board;
import com.smart.comment.controller.dto.CommentDto;
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

  public List<BoardDto.BoardInfo> getAllBoard() {
    return boardDao.getAllBoard();
  }

  public BoardDto.BoardInfo getBoardByBoardId(Long boardId) {
    BoardDto.BoardInfo boardInfo = boardDao.getBoardByBoardId(boardId).orElseThrow(() -> {
      throw new NotFoundBoardException();
    });
    return boardInfo;
  }

  public BoardDto.BoardDetail getBoardDetailByBoardId(Long boardId) {
    boardDao.updateViewCnt(boardId);
    BoardDto.BoardInfo boardInfo = boardDao.getBoardByBoardId(boardId).orElseThrow(() -> {
      throw new NotFoundBoardException();
    });
    List<CommentDto.CommentInfo> commentInfos = commentDao.getCommentsByBoardId(boardId);
    return BoardDto.BoardDetail.builder()
        .boardInfo(boardInfo)
        .commentInfos(commentInfos)
        .build();
  }

  @Transactional
  public Long createBoard(BoardDto.CreateRequest request, Long loginUserId) {
    Board board = request.toEntity(loginUserId);
    boardDao.createBoard(board);
    return board.getBoardId();
  }

  @Transactional
  public Long updateBoard(BoardDto.UpdateRequest request, Long loginUserId) {
    checkPermission(loginUserId, request.getUserId());
    checkExistBoard(request.getBoardId());
    boardDao.updateBoard(request.toEntity());
    return request.getBoardId();
  }

  @Transactional
  public void deleteByBoardId(BoardDto.DeleteRequest request, Long loginUserId) {
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
