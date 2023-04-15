package com.smart.board.controller;

import com.smart.board.controller.dto.BoardDto.BoardInfo;
import com.smart.user.domain.CustomUserDetails;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class BoardViewController {

  private final RestTemplate restTemplate = new RestTemplate();

  private String prefixUrl = "http://localhost:8080";

  @GetMapping("/boards")
  public String showBoardsPage(Model model,
      @AuthenticationPrincipal CustomUserDetails userDetails) {

    ResponseEntity<List<BoardInfo>> response = restTemplate.exchange(
        prefixUrl + "/api/v1/boards",
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<List<BoardInfo>>() {
        }
    );

    model.addAttribute("boards", response.getBody());
    model.addAttribute("userDetails", userDetails);

    return "boards";
  }

  @GetMapping("/board")
  public String showBoardDetailPage(Long boardId,
      @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

    ResponseEntity<BoardInfo> response = restTemplate.exchange(
        prefixUrl + "/api/v1/board/" + boardId,
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<BoardInfo>() {
        }
    );

    model.addAttribute("board", response.getBody());
    model.addAttribute("userDetails", userDetails);

    return "board";
  }

  @GetMapping("/board/create")
  public String showBoardCreatePage(@AuthenticationPrincipal CustomUserDetails userDetails,
      Model model) {

    model.addAttribute("userDetails", userDetails);

    return "boardCreate";
  }

  @GetMapping("/board/update")
  public String showBoardUpdatePage(Long boardId,
      @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

    ResponseEntity<BoardInfo> response = restTemplate.exchange(
        prefixUrl + "/api/v1/board/" + boardId,
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<BoardInfo>() {
        }
    );

    model.addAttribute("board", response.getBody());
    model.addAttribute("userDetails", userDetails);

    return "boardUpdate";
  }
}
