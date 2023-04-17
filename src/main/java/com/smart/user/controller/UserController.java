package com.smart.user.controller;

import com.smart.global.error.NotFoundUserException;
import com.smart.user.controller.dto.UserDto;
import com.smart.user.controller.dto.UserDto.UserInfo;
import com.smart.user.domain.User;
import com.smart.user.service.UserService;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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
  @ResponseStatus(HttpStatus.CREATED)
  public void join(@RequestBody @Valid UserDto.JoinRequest request) {
    userService.join(request);
  }

  @GetMapping("/join-auth")
  public ResponseEntity<?> join(@RequestParam String email, @RequestParam String authCode) {
    userService.verifyAuthCode(email, authCode);

    // 인증 완료 페이지로 리다이렉트
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create("/auth"));
    return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
  }

  @PutMapping("/update")
  public ResponseEntity<Boolean> updateUserNickname(@RequestBody User user) {
    boolean isUpdated = userService.updateUserInfo(user);
    return ResponseEntity.ok(isUpdated);
  }

  @GetMapping("/checkNickname")
  public ResponseEntity<Boolean> checkNickname(@RequestParam String nickname) {
    //닉네임 중복체크
    boolean isDuplicated = userService.isDuplicateNickName(nickname);
    return ResponseEntity.ok(isDuplicated);
  }
}
