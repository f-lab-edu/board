package com.smart.comment.controller;

import com.smart.comment.controller.dto.CommentDto;
import com.smart.comment.service.CommentService;
import com.smart.user.domain.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
public class CommentController {

  private final CommentService commentService;

  public CommentController(CommentService commentService) {
    this.commentService = commentService;
  }

  @PostMapping("/comment")
  @ResponseStatus(HttpStatus.CREATED)
  public void createComment(@RequestBody @Valid CommentDto.CreateRequest request,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    commentService.createComment(request, userDetails.getUserId());
  }

  @PutMapping("/comment")
  public void updateComment(@RequestBody @Valid CommentDto.UpdateRequest request,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    commentService.updateComment(request, userDetails.getUserId());
  }

  @DeleteMapping("/comment")
  public void deleteComment(@RequestBody @Valid CommentDto.DeleteRequest request,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    commentService.deleteByCommentId(request, userDetails.getUserId());
  }
}
