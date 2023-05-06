package com.smart.global.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

  @ExceptionHandler({IllegalAuthCodeException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleIllegalAuthCodeException(IllegalAuthCodeException e) {
    return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
  }

  @ExceptionHandler({DuplicatedUserEmailException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleDuplicatedUserEmailException(DuplicatedUserEmailException e) {
    return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
  }

  @ExceptionHandler({NotFoundUserException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleNotFoundUserException(NotFoundUserException e) {
    return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
  }

  @ExceptionHandler({DuplicatedUserNicknameException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleDuplicateNicknameException(DuplicatedUserNicknameException e) {
    return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
  }

  @Data
  @AllArgsConstructor
  public static class ErrorResponse {

    private int code;
    private String message;
  }
}
