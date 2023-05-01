package com.smart.user.controller.dto;

import com.smart.user.domain.Status;
import com.smart.user.domain.User;
import jakarta.validation.constraints.Email;
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
public class UserUpdateDto {

  @NotNull
  private Long userId;

  @NotBlank
  private String name;

  @NotBlank
  @Email
  private String email;

  @NotBlank
  private String password;

  @NotBlank
  private String nickname;

  public User toEntity(User user) {
    return User.builder()
        .userId(user.getUserId())
        .name(getName())
        .email(user.getEmail())
        .password(getPassword())
        .nickname(getNickname())
        .createDate(user.getCreateDate())
        .updateDate(LocalDateTime.now())
        .userStatus(user.getUserStatus())
        .role(user.getRole())
        .build();
  }
}