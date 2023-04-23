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
public class CommentCreateDto {

  @NotBlank
  private String content;

  @NotNull
  private Long postId;

  public Comment toEntity(Long loginUserId) {
    return Comment.builder()
        .content(getContent())
        .createDate(LocalDateTime.now())
        .postId(getPostId())
        .userId(loginUserId)
        .build();
  }
}
