package com.smart.board.controller.dto.post;

import com.smart.board.domain.Post;
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

  public static PostReadDto toDto(Post post, User user) {
    return PostReadDto.builder()
        .postId(post.getPostId())
        .title(post.getTitle())
        .content(post.getContent())
        .createDate(post.getCreateDate())
        .updateDate(post.getUpdateDate())
        .viewCount(post.getViewCount())
        .userId(user.getUserId())
        .nickname(user.getNickname())
        .build();
  }

}
