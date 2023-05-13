package com.smart.global.error;

public class InvalidUserInfoException extends RuntimeException {

  public InvalidUserInfoException(final String message) {
    super(message);
  }

  public InvalidUserInfoException() {
    this("잘못된 회원의 정보입니다.");
  }
}
