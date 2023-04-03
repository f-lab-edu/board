package com.smart.user.controller;

import com.smart.user.controller.dto.UserDto;
import com.smart.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
  public UserDto.Response getUser(@PathVariable("email") String email) {
    return userService.getUserByEmail(email);
  }

  @PostMapping("/join")
  @ResponseStatus(HttpStatus.CREATED)
  public void join(@RequestBody @Valid UserDto.JoinRequest request) {
    userService.join(request);
  }

  @GetMapping("/join-auth")
  public void join(@RequestParam String email, @RequestParam String authCode) {
    userService.verifyAuthCode(email, authCode);
  }
}
