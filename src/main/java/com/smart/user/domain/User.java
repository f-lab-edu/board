package com.smart.user.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class User {

  private Long userId;
  private String name;
  private String email;
  private String password;
  private String nickName;
  private LocalDateTime createDate;
  private LocalDateTime updateDate;
  private Status userStatus;

  @Builder
  public User(Long userId, String name, String email, String password, String nickName,
      LocalDateTime createDate, LocalDateTime updateDate, Status userStatus) {
    this.userId = userId;
    this.name = name;
    this.email = email;
    this.password = password;
    this.nickName = nickName;
    this.createDate = createDate;
    this.updateDate = updateDate;
    this.userStatus = userStatus;
  }

}
