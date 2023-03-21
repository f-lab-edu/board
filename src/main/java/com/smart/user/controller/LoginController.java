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
  public String login(@RequestParam String username, @RequestParam String password) {
    System.out.println("Username: " + username);
    System.out.println("Password: " + password);
    return "redirect:/";
  }

  @GetMapping("/mypage")
  public String showLoginSuccessPage(){
    return "mypage";
  }

}
