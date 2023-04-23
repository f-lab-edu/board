package com.smart.board.controller.dto.post;

import com.smart.board.domain.Post;
import jakarta.validation.constraints.NotBlank;
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
public class PostCreateDto {

  @NotBlank
  private String title;

  @NotBlank
  private String content;

  public Post toEntity(Long loginUserId) {
    return Post.builder()
        .title(getTitle())
        .content(getContent())
        .createDate(LocalDateTime.now())
        .viewCount(0L)
        .userId(loginUserId)
        .build();
  }
}
