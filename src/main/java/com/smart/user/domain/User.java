package com.smart.user.domain;

import com.smart.global.error.InvalidUserInfoException;
import java.time.LocalDateTime;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

  private static final int MAX_NICKNAME_LENGTH = 15;
  private static final String PASSWORD_FORMAT = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,15}$";
  private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_FORMAT);
  private Long userId;
  private String name;
  private String email;
  private String password;
  private String nickname;
  private LocalDateTime createDate;
  private LocalDateTime updateDate;
  private Status userStatus;
  private String role;

  @Builder
  public User(Long userId, String name, String email, String password, String nickname,
      LocalDateTime createDate, LocalDateTime updateDate, Status userStatus, String role) {
    this.userId = userId;
    this.name = name;
    this.email = email;
    validatePassword(password);
    this.password = password;
    validateNickname(nickname);
    this.nickname = nickname;
    this.createDate = createDate;
    this.updateDate = updateDate;
    this.userStatus = userStatus;
    this.role = role;
  }

  public void updateUserId(Long userId) {
    this.userId = userId;
  }

  public void approveUserAuth() {
    this.userStatus = Status.NORMAL;
  }

  private void validateNickname(final String nickname) {
    if (nickname.isEmpty() || nickname.length() > MAX_NICKNAME_LENGTH) {
      throw new InvalidUserInfoException(String.format("닉네임은 1자 이상 1자 %d자까지 사용 가능합니다", MAX_NICKNAME_LENGTH));
    }
  }

  private void validatePassword(final String password){
    if(!PASSWORD_PATTERN.matcher(password).matches()){
      throw new InvalidUserInfoException("비밀번호는 8자에서 15자여야 하며, 대소문자 포함 영문자와 숫자, 특수문자를 하나 이상 포함해야 합니다.");
    }
  }

  public void updateTemporaryPassword(String temporaryPassword) {
    this.password = temporaryPassword;
  }
}