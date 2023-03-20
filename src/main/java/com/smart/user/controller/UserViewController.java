package com.smart.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserViewController {

  @GetMapping("/join")
  public String showJoinPage() {
    return "user/join";
  }
}
