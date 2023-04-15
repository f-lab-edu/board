package com.smart;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

  @GetMapping("/")
  public String showMainPage() {
    return "redirect:/boards";
  }
}

