package com.smart.user.controller;

import com.smart.user.controller.dto.UserInfoDto;
import com.smart.user.controller.dto.UserSaveDto;
import com.smart.user.controller.dto.UserUpdateDto;
import com.smart.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/{email}/exist")
  public ResponseEntity<UserInfoDto> getUserByEmail(@PathVariable("email") String email) {
    UserInfoDto user = userService.getUserByEmail(email);
    return ResponseEntity.ok(user);
  }

  @PostMapping("/join")
  public ResponseEntity<Void> join(@RequestBody @Valid UserSaveDto request) {
    userService.join(request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/join-auth")
  public ResponseEntity<Void> join(@RequestParam String email, @RequestParam String authCode) {
    userService.verifyAuthCode(email, authCode);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PutMapping("/update")
  public ResponseEntity<Void> updateUserNickname(@RequestBody UserUpdateDto userUpdateDto) {
    userService.updateUserInfo(userUpdateDto);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("/users/nickname/{nickname}/exist")
  public ResponseEntity<Void> checkNickname(@PathVariable String nickname) {
    userService.checkDuplicateNickname(nickname);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PostMapping("/reset-password")
  public ResponseEntity<Void> sendPasswordResetCodeEmail(@RequestParam String userEmail) {
    userService.resetPassword(userEmail);
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
