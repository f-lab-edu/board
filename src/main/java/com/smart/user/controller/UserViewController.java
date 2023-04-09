package com.smart.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserViewController {

  @GetMapping("/join")
  public String showJoinPage() {
    return "join";
  }

  @GetMapping("/login")
  public String showLoginPage() {
    return "login";
  }

  @GetMapping("/auth")
  public String showAuthPage() {
    return "auth";
  }

  @GetMapping("/mypage")
  public String showLoginSuccessPage(){
    return "mypage";
  }

}
