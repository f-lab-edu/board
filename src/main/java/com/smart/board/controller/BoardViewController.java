package com.smart.board.controller;

import com.smart.board.controller.dto.BoardDto.BoardDetail;
import com.smart.board.service.BoardService;
import com.smart.comment.service.CommentService;
import com.smart.user.domain.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardViewController {

  private final BoardService boardService;
  private final CommentService commentService;

  public BoardViewController(BoardService boardService, CommentService commentService) {
    this.boardService = boardService;
    this.commentService = commentService;
  }

  @GetMapping("/boards")
  public String showBoardsPage(Model model,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    model.addAttribute("boards", boardService.getAllBoard());
    model.addAttribute("userDetails", userDetails);
    return "boards";
  }

  @GetMapping("/board")
  public String showBoardDetailPage(Long boardId,
      @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
    BoardDetail boardDetail = boardService.getBoardDetailByBoardId(boardId);
    model.addAttribute("board", boardDetail.getBoardInfo());
    model.addAttribute("comments", boardDetail.getCommentInfos());
    model.addAttribute("userDetails", userDetails);
    return "board";
  }

  @GetMapping("/board/create")
  public String showBoardCreatePage(@AuthenticationPrincipal CustomUserDetails userDetails,
      Model model) {
    model.addAttribute("userDetails", userDetails);
    return "boardCreate";
  }

  @GetMapping("/board/update")
  public String showBoardUpdatePage(Long boardId,
      @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
    model.addAttribute("board", boardService.getBoardByBoardId(boardId));
    model.addAttribute("userDetails", userDetails);
    return "boardUpdate";
  }
}
