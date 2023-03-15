package com.smart.user.controller.dto;

import com.smart.user.domain.Status;
import com.smart.user.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDto {

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor @Builder
  public static class JoinRequest {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String nickname;

    public User toEntity() {
      return User.builder()
          .name(getName())
          .email(getEmail())
          .password(getPassword())
          .nickname(getNickname())
          .createDate(LocalDateTime.now())
          .userStatus(Status.NORMAL)
          .build();
    }
  }
}
