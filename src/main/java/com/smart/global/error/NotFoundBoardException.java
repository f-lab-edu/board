package com.smart.global.error;

public class NotFoundBoardException extends RuntimeException {

  private static final String MESSAGE = "해당 게시물을 찾을 수 없습니다.";

  public NotFoundBoardException() {
    super(MESSAGE);
  }
}
