package com.smart.board.controller;

import com.smart.board.service.BoardService;
import com.smart.user.domain.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardViewController {

  private final BoardService boardService;

  public BoardViewController(BoardService boardService) {
    this.boardService = boardService;
  }

  @GetMapping("/boards")
  public String showBoardsPage(Model model,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    model.addAttribute("boardList", boardService.getPosts());
    model.addAttribute("userDetails", userDetails);
    return "boards";
  }

  @GetMapping("/board")
  public String showBoardDetailPage(Long postId,
      @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
    model.addAttribute("post", boardService.getPostByPostId(postId));
    model.addAttribute("comments", boardService.getCommentsByPostId(postId));
    model.addAttribute("userDetails", userDetails);
    return "board";
  }

  @GetMapping("/board/create")
  public String showBoardCreatePage() {
    return "boardCreate";
  }

  @GetMapping("/board/update")
  public String showBoardUpdatePage(Long postId, Model model) {
    model.addAttribute("post", boardService.getPostByPostId(postId));
    return "boardUpdate";
  }
}
