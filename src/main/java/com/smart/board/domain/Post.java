package com.smart.board.domain;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Post {

  private Long postId;
  private String title;
  private String content;
  private LocalDateTime createDate;
  private LocalDateTime updateDate;
  private Long viewCount;
  private Long userId;

  public void setPostId(Long postId) {
    this.postId = postId;
  }

  public void updateViewCount(Long userId) {
    if (!this.userId.equals(userId)) {
      viewCount++;
    }
  }
}
