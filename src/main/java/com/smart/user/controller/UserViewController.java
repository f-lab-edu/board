package com.smart.user.controller;

import com.smart.security.CustomUserDetails;
import com.smart.security.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserViewController {

  private final UserSecurityService userSecurityService;

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
  public String showLoginSuccessPage(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
    UserDetails loadedUserDetails = userSecurityService.loadUserByUsername(userDetails.getEmail());
    model.addAttribute("user", loadedUserDetails);
    return "mypage";
  }

  @GetMapping("/edit")
  public String edit(Model model
      ,@AuthenticationPrincipal CustomUserDetails userDetails) {
    model.addAttribute("user", userDetails);
    return "user_edit";
  }

}
