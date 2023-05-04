package com.smart.board.controller;

import com.smart.board.controller.dto.comment.CommentCreateDto;
import com.smart.board.controller.dto.comment.CommentReadDto;
import com.smart.board.controller.dto.comment.CommentUpdateDto;
import com.smart.board.controller.dto.post.PostCreateDto;
import com.smart.board.controller.dto.post.PostReadDto;
import com.smart.board.controller.dto.post.PostUpdateDto;
import com.smart.board.service.BoardService;
import com.smart.security.AuthUserId;
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
  public PostReadDto getPostByPostId(@PathVariable Long postId, @AuthUserId Long loginUserId) {
    return boardService.getPostByPostId(postId, loginUserId);
  }

  @PostMapping("/post")
  @ResponseStatus(HttpStatus.CREATED)
  public Long createPost(@RequestBody @Valid PostCreateDto createDto,
      @AuthUserId Long loginUserId) {
    return boardService.createPost(createDto, loginUserId);
  }

  @PutMapping("/post")
  public void updatePost(@RequestBody @Valid PostUpdateDto updateDto,
      @AuthUserId Long loginUserId) {
    boardService.updatePost(updateDto, loginUserId);
  }

  @DeleteMapping("/post/{postId}")
  public void deletePost(@PathVariable Long postId, @AuthUserId Long loginUserId) {
    boardService.deletePost(postId, loginUserId);
  }

  @GetMapping("/comment/{postId}")
  public List<CommentReadDto> getCommentsByPostId(@PathVariable Long loginUserId) {
    return boardService.getCommentsByPostId(loginUserId);
  }

  @PostMapping("/comment")
  @ResponseStatus(HttpStatus.CREATED)
  public Long createComment(@RequestBody @Valid CommentCreateDto createDto,
      @AuthUserId Long loginUserId) {
    return boardService.createComment(createDto, loginUserId);
  }

  @PutMapping("/comment")
  public void updateComment(@RequestBody @Valid CommentUpdateDto updateDto,
      @AuthUserId Long loginUserId) {
    boardService.updateComment(updateDto, loginUserId);
  }

  @DeleteMapping("/comment/{commentId}")
  public void deleteComment(@PathVariable Long commentId, @AuthUserId Long loginUserId) {
    boardService.deleteComment(commentId, loginUserId);
  }
}
