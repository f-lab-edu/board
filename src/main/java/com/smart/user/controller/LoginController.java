package com.smart.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

  @GetMapping("/login")
  public String showLoginPage() {
    return "login";
  }

  @PostMapping("/login")
  public String login(@RequestParam String email
      , @RequestParam String password) {
    System.out.println("Username: " + email);
    System.out.println("Password: " + password);
    return "redirect:/login";
  }

  @GetMapping("/mypage")
  public String showLoginSuccessPage(){
    return "mypage";
  }

}
