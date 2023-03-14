package com.smart.user.controller;

import com.smart.user.domain.User;
import com.smart.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/users/{name}/exist")
  public User getUser(@PathVariable("name") String name){
    System.out.println(userService.getUser(name).getName());
    return userService.getUser(name);
  }
}
