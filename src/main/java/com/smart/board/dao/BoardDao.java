package com.smart.board.dao;

import com.smart.board.controller.dto.BoardDto.BoardInfo;
import com.smart.board.domain.Board;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardDao {

  List<BoardInfo> getAllBoard();

  Optional<BoardInfo> getBoardByBoardId(Long boardId);

  void updateViewCnt(Long boardId);

  void createBoard(Board board);

  void updateBoard(Board board);

  void deleteByBoardId(Long boardId);

  boolean checkBoardId(Long boardId);
}
