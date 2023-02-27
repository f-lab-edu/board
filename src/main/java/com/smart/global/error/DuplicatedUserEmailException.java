package com.smart.global.error;

public class DuplicatedUserEmailException extends RuntimeException {

  private static final String MESSAGE = "이메일이 중복됩니다.";

  public DuplicatedUserEmailException() {
    super(MESSAGE);
  }
}