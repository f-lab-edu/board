package com.smart.global;

import com.smart.global.error.DuplicatedUserEmailException;
import com.smart.global.error.DuplicatedUserNicknameException;
import com.smart.global.error.IllegalAuthCodeException;
import com.smart.global.error.InvalidUserInfoException;
import com.smart.global.error.NotFoundEntityException;
import com.smart.global.error.PermissionDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DuplicatedUserEmailException.class)
  public ResponseEntity<String> handleDuplicatedUserEmailException(
      DuplicatedUserEmailException exception) {
    return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(DuplicatedUserNicknameException.class)
  public ResponseEntity<String> handleDuplicatedUserNicknameException(
      DuplicatedUserNicknameException exception) {
    return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(IllegalAuthCodeException.class)
  public ResponseEntity<String> handleIllegalAuthCodeException(IllegalAuthCodeException exception) {
    return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NotFoundEntityException.class)
  public ResponseEntity<String> handleNotFoundEntityException(NotFoundEntityException exception) {
    return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(PermissionDeniedException.class)
  public ResponseEntity<String> handlePermissionDeniedException(
      PermissionDeniedException exception) {
    return new ResponseEntity<>(exception.getMessage(), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(InvalidUserInfoException.class)
  public ResponseEntity<String> handleInvalidUserInfoException(
      InvalidUserInfoException exception) {
    return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
  }
}
