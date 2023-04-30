package com.smart.board.controller.dto.comment;

import com.smart.board.domain.Comment;
import com.smart.user.domain.User;
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
public class CommentReadDto {

  @NotNull
  private Long commentId;

  @NotBlank
  private String content;

  @NotBlank
  private LocalDateTime createDate;

  private LocalDateTime updateDate;

  @NotNull
  private Long postId;

  @NotNull
  private Long userId;

  @NotBlank
  private String nickname;

  public static CommentReadDto toDto(Comment comment, User user) {
    return CommentReadDto.builder()
        .commentId(comment.getCommentId())
        .content(comment.getContent())
        .createDate(comment.getCreateDate())
        .updateDate(comment.getUpdateDate())
        .postId(comment.getPostId())
        .userId(user.getUserId())
        .nickname(user.getNickname())
        .build();
  }
}
