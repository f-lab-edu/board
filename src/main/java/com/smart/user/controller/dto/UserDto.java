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

/**
 * dto inner 클래스
 * - dto 클래스 파일의 개수를 줄일 수 있으며 도메인별로 정리가 가능해 응집도가 높아진다는 장점이 있다.
 */
public class UserDto {

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor
  @Builder
  public static class Response {

    @NotBlank
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

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    @NotBlank
    private Status userStatus;

    private String role;

    static public Response toResponse(User user) {
      return Response.builder()
          .userId(user.getUserId())
          .name(user.getName())
          .email(user.getEmail())
          .password(user.getPassword())
          .nickname(user.getNickname())
          .createDate(user.getCreateDate())
          .updateDate(user.getUpdateDate())
          .userStatus(user.getUserStatus())
          .role(user.getRole())
          .build();
    }
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor
  @Builder
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
    private String role;

    public User toEntity() {
      return User.builder()
          .name(getName())
          .email(getEmail())
          .password(getPassword())
          .nickname(getNickname())
          .createDate(LocalDateTime.now())
          .userStatus(Status.NORMAL)
          .role(getRole())
          .build();
    }
  }
}