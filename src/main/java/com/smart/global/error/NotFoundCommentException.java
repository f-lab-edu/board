package com.smart.global.error;

public class NotFoundCommentException extends RuntimeException {

  private static final String MESSAGE = "해당 댓글을 찾을 수 없습니다.";

  public NotFoundCommentException() {
    super(MESSAGE);
  }
}
