package com.smart.board.controller;

import com.smart.board.controller.dto.BoardDto;
import com.smart.board.controller.dto.BoardDto.BoardInfo;
import com.smart.board.controller.dto.BoardDto.DeleteRequest;
import com.smart.board.service.BoardService;
import com.smart.user.domain.CustomUserDetails;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RequestMapping("/api/v1")
public class BoardController {

  private final BoardService boardService;

  public BoardController(BoardService boardService) {
    this.boardService = boardService;
  }

  @GetMapping("/boards")
  public List<BoardInfo> getAllBoard() {
    return boardService.getAllBoard();
  }

  @GetMapping("/board/{boardId}")
  public BoardDto.BoardInfo getBoard(@PathVariable("boardId") Long boardId) {
    return boardService.getBoardByBoardId(boardId);
  }

  @PostMapping("/board")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<Void> createBoard(@RequestBody @Valid BoardDto.CreateRequest request,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    Long boardId = boardService.createBoard(request, userDetails.getUserId());
    return ResponseEntity.created(URI.create("/board?boardId=" + boardId)).build();
  }

  @PutMapping("/board")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateBoard(@RequestBody @Valid BoardDto.UpdateRequest request,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    boardService.updateBoard(request, userDetails.getUserId());
  }

  @DeleteMapping("/board")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteBoard(@RequestBody @Valid DeleteRequest request,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    boardService.deleteByBoardId(request, userDetails.getUserId());
  }
}
