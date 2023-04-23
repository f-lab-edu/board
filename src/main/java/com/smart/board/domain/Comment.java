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
public class Comment {

  private Long commentId;
  private String content;
  private LocalDateTime createDate;
  private LocalDateTime updateDate;
  private Long postId;
  private Long userId;
}
