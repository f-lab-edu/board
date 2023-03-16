package com.smart.global.error;

public class NotFoundUserException extends RuntimeException {

  private static final String MESSAGE = "해당 유저를 찾을 수 없습니다.";

  public NotFoundUserException() {
    super(MESSAGE);
  }
}