package com.smart.board.controller.dto.comment;

import com.smart.board.domain.Comment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CommentUpdateDto {

  @NotNull
  private Long commentId;

  @NotBlank
  private String content;

  public Comment toEntity(Comment comment) {
    return Comment.builder()
        .commentId(getCommentId())
        .content(getContent())
        .createDate(comment.getCreateDate())
        .updateDate(LocalDateTime.now())
        .postId(comment.getPostId())
        .userId(comment.getUserId())
        .build();
  }
}
