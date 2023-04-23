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

  @NotNull
  private Long userId;

  public Comment toEntity() {
    return Comment.builder()
        .commentId(getCommentId())
        .content(getContent())
        .updateDate(LocalDateTime.now())
        .userId(getUserId())
        .build();
  }
}
