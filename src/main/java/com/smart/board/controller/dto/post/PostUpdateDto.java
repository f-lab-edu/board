package com.smart.board.controller.dto.post;

import com.smart.board.domain.Post;
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
public class PostUpdateDto {

  @NotNull
  private Long postId;

  @NotBlank
  private String title;

  @NotBlank
  private String content;

  public Post toEntity(Post post) {
    return Post.builder()
        .postId(getPostId())
        .title(getTitle())
        .content(getContent())
        .createDate(post.getCreateDate())
        .updateDate(LocalDateTime.now())
        .viewCount(post.getViewCount())
        .userId(post.getUserId())
        .build();
  }
}
