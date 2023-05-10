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

  public void updateUserId(Long userId) {
    this.userId = userId;
  }

  public void approveUserAuth() {
    this.userStatus = Status.NORMAL;
  }

  public void updateName(String name) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("이름은 필수 입력 사항입니다.");
    }
    this.name = name;
  }

  public void updatePassword(String password) {
    this.password = password;
  }

  public void updateNickname(String nickname) {
    if (nickname == null || nickname.isBlank()) {
      throw new IllegalArgumentException("닉네임은 필수 입력 사항입니다.");
    }
    this.nickname = nickname;
  }
}