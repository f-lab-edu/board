package com.smart.user.controller;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.smart.user.controller.dto.UserDto.JoinRequest;
import com.smart.user.dao.UserDao;
import com.smart.user.domain.User;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LoginControllerTest {

  @Autowired
  private UserDao userDao;
  private MockMvc mockMvc;
  private User user;

  @BeforeEach
  public void setUp() {
    JoinRequest joinRequest = JoinRequest.builder()
        .name("name")
        .nickname("nickname")
        .password("123")
        .email("test@email")
        .role("USER").build();
    user = joinRequest.toEntity();
    mockMvc = MockMvcBuilders.standaloneSetup(new LoginController()).build(); // MockMvc 객체 생성 및 초기화
  }

  @AfterEach
  public void deletePreUser() {
    User preUser = userDao.getUserByEmail(user.getEmail());
    if (preUser != null) {
      userDao.deleteUserByEmail(preUser.getEmail());
    }
  }

  @Test
  public void testLoginSuccess() throws Exception {
    userDao.joinUser(user);
    ResultActions resultActions = mockMvc.perform(post("/login")
            .param("email", user.getEmail())
            .param("password", user.getPassword()))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/mypage"))
        .andDo(print());

    HttpSession session = resultActions.andReturn().getRequest().getSession();
    assertNotNull(session);
  }

  @Test
  public void testLoginFail() throws Exception{
    userDao.joinUser(user);
    ResultActions resultActions = mockMvc.perform(post("/login")
            .param("email", user.getEmail())
            .param("password", "wrongPassword"))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/login"))
        .andDo(print());

    HttpSession session = resultActions.andReturn().getRequest().getSession();
    assertNotNull(session);
  }
}
