package com.smart.board.controller.dto.post;

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
public class PostReadDto {

  @NotNull
  private Long postId;

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
