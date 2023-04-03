package com.smart.global.error;

public class IllegalAuthCodeException extends RuntimeException{

  private static final String MESSAGE = "유효하지않은 인증코드입니다.";

  public IllegalAuthCodeException() {
    super(MESSAGE);
  }

}
