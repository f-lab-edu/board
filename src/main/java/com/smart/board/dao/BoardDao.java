package com.smart.board.dao;

import com.smart.board.controller.dto.BoardDto;
import com.smart.board.domain.Board;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardDao {

  List<BoardDto.Response> getAllBoard();

  BoardDto.Response getBoardByBoardId(Long boardId);

  void createBoard(Board board);

  void updateBoard(Board board);

  void deleteByBoardId(Long boardId);

  boolean checkBoardId(Long boardId);
}
