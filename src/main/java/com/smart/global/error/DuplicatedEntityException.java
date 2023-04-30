package com.smart.global.error;

public class DuplicatedEntityException extends RuntimeException {

  public DuplicatedEntityException(String entity) {
    super(entity + "이 중복됩니다.");
  }
}
