package com.smart.user.controller.dto;

import com.smart.user.domain.Status;
import com.smart.user.domain.User;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

  private Long userId;
  private String name;
  private String email;
  private String password;
  private String nickName;
  private LocalDateTime createDate;
  private LocalDateTime updateDate;
  private Status userStatus;

  @Builder
  public UserDto(Long userId, String name, String email, String password, String nickName,
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

  static UserDto of(User user) {
    return UserDto.builder()
        .userId(user.getUserId())
        .name(user.getName())
        .email(user.getEmail())
        .password(user.getPassword())
        .nickName(user.getNickName())
        .createDate(user.getCreateDate())
        .updateDate(user.getUpdateDate())
        .userStatus(user.getUserStatus())
        .build();
  }

  public User toEntity() {
    return User.builder()
        .name(this.getName())
        .email(this.getEmail())
        .password(this.getPassword())
        .nickName(this.getNickName())
        .userStatus(this.getUserStatus())
        .build();
  }
}
