package com.smart.user.controller.dto;

import com.smart.user.domain.Status;
import com.smart.user.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserInfoDto {

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

  @NotBlank
  private Status userStatus;

  private String role;

  public static UserInfoDto from(User user) {
    return UserInfoDto.builder()
        .userId(user.getUserId())
        .name(user.getName())
        .email(user.getEmail())
        .password(user.getPassword())
        .nickname(user.getNickname())
        .userStatus(user.getUserStatus())
        .role(user.getRole())
        .build();
  }
}
