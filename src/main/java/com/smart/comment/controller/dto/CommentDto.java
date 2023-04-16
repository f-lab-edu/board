package com.smart.comment.controller.dto;

import com.smart.comment.domain.Comment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CommentDto {

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor
  @Builder
  public static class CommentInfo {

    @NotNull
    private Long commentId;

    @NotBlank
    private String content;

    @NotBlank
    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    @NotNull
    private Long boardId;

    @NotNull
    private Long userId;

    @NotBlank
    private String nickname;
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor
  @Builder
  public static class CreateRequest {

    @NotBlank
    private String content;

    @NotNull
    private Long boardId;

    public Comment toEntity(Long loginUserId) {
      return Comment.builder()
          .content(getContent())
          .createDate(LocalDateTime.now())
          .boardId(getBoardId())
          .userId(loginUserId)
          .build();
    }
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor
  @Builder
  public static class UpdateRequest {

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

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor
  @Builder
  public static class DeleteRequest {

    @NotNull
    private Long commentId;

    @NotNull
    private Long userId;
  }
}

