package com.smart.user.domain;

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
public class User {

  private Long userId;
  private String name;
  private String email;
  private String password;
  private String nickname;
  private LocalDateTime createDate;
  private LocalDateTime updateDate;
  private Status userStatus;
  private String role;

  public void setUserId(Long userId) {
    this.userId = userId;
  }
}
