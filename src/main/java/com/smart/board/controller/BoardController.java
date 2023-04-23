package com.smart.board.controller;

import com.smart.board.controller.dto.BoardDetailDto;
import com.smart.board.controller.dto.BoardListDto;
import com.smart.board.controller.dto.comment.CommentCreateDto;
import com.smart.board.controller.dto.comment.CommentDeleteDto;
import com.smart.board.controller.dto.comment.CommentReadDto;
import com.smart.board.controller.dto.comment.CommentUpdateDto;
import com.smart.board.controller.dto.post.PostCreateDto;
import com.smart.board.controller.dto.post.PostDeleteDto;
import com.smart.board.controller.dto.post.PostReadDto;
import com.smart.board.controller.dto.post.PostUpdateDto;
import com.smart.board.service.BoardService;
import com.smart.user.domain.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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

  @GetMapping("/board")
  public BoardListDto getBoardList() {
    return boardService.getBoardList();
  }

  @GetMapping("/board/{postId}")
  public BoardDetailDto getBoardDetailByPostId(@PathVariable Long postId) {
    return boardService.getBoardDetailByPostId(postId);
  }

  @GetMapping("/post/{postId}")
  public PostReadDto getPostByPostId(@PathVariable Long postId) {
    return boardService.getPostByPostId(postId);
  }

  @PostMapping("/post")
  @ResponseStatus(HttpStatus.CREATED)
  public Long createPost(@RequestBody @Valid PostCreateDto createDto,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    return boardService.createPost(createDto, userDetails.getUserId());
  }

  @PutMapping("/post")
  public void updatePost(@RequestBody @Valid PostUpdateDto updateDto,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    boardService.updatePost(updateDto, userDetails.getUserId());
  }

  @DeleteMapping("/post")
  public void deletePost(@RequestBody @Valid PostDeleteDto deleteDto,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    boardService.deletePost(deleteDto, userDetails.getUserId());
  }

  @GetMapping("/comment/{commentId}")
  public CommentReadDto getCommentByCommentId(@PathVariable Long commentId) {
    return boardService.getCommentByCommentId(commentId);
  }

  @PostMapping("/comment")
  @ResponseStatus(HttpStatus.CREATED)
  public Long createComment(@RequestBody @Valid CommentCreateDto createDto,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    return boardService.createComment(createDto, userDetails.getUserId());
  }

  @PutMapping("/comment")
  public void updateComment(@RequestBody @Valid CommentUpdateDto updateDto,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    boardService.updateComment(updateDto, userDetails.getUserId());
  }

  @DeleteMapping("/comment")
  public void deleteComment(@RequestBody @Valid CommentDeleteDto deleteDto,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    boardService.deleteComment(deleteDto, userDetails.getUserId());
  }
}
