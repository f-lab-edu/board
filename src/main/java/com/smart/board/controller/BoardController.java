package com.smart.board.controller;

import com.smart.board.controller.dto.comment.CommentCreateDto;
import com.smart.board.controller.dto.comment.CommentReadDto;
import com.smart.board.controller.dto.comment.CommentUpdateDto;
import com.smart.board.controller.dto.post.PostCreateDto;
import com.smart.board.controller.dto.post.PostReadDto;
import com.smart.board.controller.dto.post.PostUpdateDto;
import com.smart.board.service.BoardService;
import com.smart.user.domain.CustomUserDetails;
import jakarta.validation.Valid;
import java.util.List;
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

  @GetMapping("/posts")
  public List<PostReadDto> getPosts() {
    return boardService.getPosts();
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

  @DeleteMapping("/post/{postId}")
  public void deletePost(@PathVariable Long postId,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    boardService.deletePost(postId, userDetails.getUserId());
  }

  @GetMapping("/comment/{postId}")
  public List<CommentReadDto> getCommentsByPostId(@PathVariable Long postId) {
    return boardService.getCommentsByPostId(postId);
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

  @DeleteMapping("/comment/{commentId}")
  public void deleteComment(@PathVariable Long commentId,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    boardService.deleteComment(commentId, userDetails.getUserId());
  }
}
