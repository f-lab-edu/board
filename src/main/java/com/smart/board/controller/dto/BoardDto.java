package com.smart.board.controller.dto;

import com.smart.board.domain.Board;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BoardDto {

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor
  @Builder
  public static class BoardInfo {

    @NotNull
    private Long boardId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    @NotNull
    private Long viewCount;

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
    private String title;

    @NotBlank
    private String content;

    public Board toEntity(Long loginUserId) {
      return Board.builder()
          .title(getTitle())
          .content(getContent())
          .createDate(LocalDateTime.now())
          .viewCount(0L)
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
    private Long boardId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private Long userId;

    public Board toEntity() {
      return Board.builder()
          .boardId(getBoardId())
          .title(getTitle())
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
    private Long boardId;

    @NotNull
    private Long userId;
  }
}
