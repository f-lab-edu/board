package com.smart.global.error;

public class DuplicatedUserNicknameException extends RuntimeException {

  private static final String MESSAGE = "닉네임이 중복됩니다.";

  public DuplicatedUserNicknameException() {
    super(MESSAGE);
  }
}