package com.smart.global.error;

public class PermissionDeniedBoardException extends RuntimeException{

  private static final String MESSAGE = "해당 게시물 권한이 없습니다.";

  public PermissionDeniedBoardException() {
    super(MESSAGE);
  }

}
