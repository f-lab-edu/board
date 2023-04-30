package com.smart.user.controller;

import com.smart.global.error.NotFoundUserException;
import com.smart.user.controller.dto.UserDto;
import com.smart.user.controller.dto.UserDto.UserInfo;
import com.smart.user.domain.User;
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
  public ResponseEntity<UserInfo> getUser(@PathVariable("email") String email) {
    try {
      UserInfo userInfo = userService.getUserByEmail(email);
      return ResponseEntity.ok(userInfo);
    } catch (NotFoundUserException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping("/join")
  public ResponseEntity<User> join(@RequestBody @Valid UserDto.JoinRequest request) {
    User user = userService.join(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(user);
  }

  @GetMapping("/join-auth")
  public ResponseEntity<Void> join(@RequestParam String email, @RequestParam String authCode) {
    userService.verifyAuthCode(email, authCode);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PutMapping("/update")
  public ResponseEntity<Void> updateUserNickname(@RequestBody User user) {
    userService.updateUserInfo(user);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("/users/nickname/{nickname}/exist")
  public ResponseEntity<Void> checkNickname(@PathVariable String nickname) {
    userService.isDuplicateNickname(nickname);
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
