package com.smart.board.controller;

import com.smart.board.controller.dto.BoardDto;
import com.smart.board.service.BoardService;
import com.smart.user.domain.CustomUserDetails;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
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

  @PostMapping("/board")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<Void> createBoard(@RequestBody @Valid BoardDto.CreateRequest request,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    Long boardId = boardService.createBoard(request, userDetails.getUserId());
    return ResponseEntity.created(URI.create("/board?boardId=" + boardId)).build();
  }

  @PutMapping("/board")
  public void updateBoard(@RequestBody @Valid BoardDto.UpdateRequest request,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    boardService.updateBoard(request, userDetails.getUserId());
  }

  @DeleteMapping("/board")
  public void deleteBoard(@RequestBody @Valid BoardDto.DeleteRequest request,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    boardService.deleteByBoardId(request, userDetails.getUserId());
  }
}
