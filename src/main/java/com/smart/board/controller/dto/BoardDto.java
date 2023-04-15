package com.smart.board.controller.dto;

import com.smart.board.domain.Board;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    private Long boardId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    @NotBlank
    private Long viewCount;

    @NotBlank
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

    @NotBlank
    private Long boardId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
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

    @NotBlank
    private Long boardId;

    @NotBlank
    private Long userId;
  }
}
