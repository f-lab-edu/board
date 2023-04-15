package com.smart;

import com.smart.user.domain.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

  @GetMapping("/")
  public String showMainPage(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
    String name = userDetails != null ? userDetails.getName() : "anonymous";
    model.addAttribute("username", name);
    return "main";
  }
}

