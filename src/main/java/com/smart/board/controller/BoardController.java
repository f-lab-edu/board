package com.smart.board.controller;

import com.smart.board.controller.dto.BoardDto;
import com.smart.board.controller.dto.BoardDto.BoardInfo;
import com.smart.board.service.BoardService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/board")
public class BoardController {

  private final BoardService boardService;

  public BoardController(BoardService boardService) {
    this.boardService = boardService;
  }

  @GetMapping("/boards")
  public List<BoardInfo> getAllBoard() {
    return boardService.getAllBoard();
  }

  @GetMapping("/board")
  public BoardDto.BoardInfo getBoard(@PathVariable("boardId") Long boardId) {
    return boardService.getBoardByBoardId(boardId);
  }

  @PostMapping("/boards")
  @ResponseStatus(HttpStatus.CREATED)
  public Long createBoard(@RequestBody @Valid BoardDto.CreateRequest request) {
    return null;
  }

  @PutMapping("/board")
  public Long updateBoard(@PathVariable("boardId") Long boardId) {
    return null;
  }

  @DeleteMapping("/board")
  public void deleteBoard(@PathVariable("boardId") Long boardId) {

  }
}
