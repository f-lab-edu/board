package com.smart.user.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.smart.user.dao.UserDao;
import com.smart.user.domain.Status;
import com.smart.user.domain.User;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@Transactional
public class SecurityIntegrationTest {
  @Autowired
  private WebApplicationContext context;
  @Autowired
  private UserDao userDao;
  private MockMvc mockMvc;
  private User user;

  @BeforeEach
  public void setup(){

    mockMvc = MockMvcBuilders
        .webAppContextSetup(this.context)
        .apply(springSecurity())
        .build();

    user = User.builder()
        .userId(9999L)
        .name("test")
        .nickname("test")
        .email("test@gmail.com")
        .password("1234")
        .role("USER")
        .userStatus(Status.NORMAL)
        .createDate(LocalDateTime.now())
        .build();
    userDao.joinUser(user);
  }

  @Test
  public void testLoginSuccess() throws Exception {
    ResultActions resultActions = mockMvc.perform(formLogin("/login")
            .user("email", user.getEmail())
            .password("password", user.getPassword()))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/mypage"))
        .andDo(print());

    HttpSession session = resultActions.andReturn().getRequest().getSession();
    assertNotNull(session);
  }

  @Test
  public void testLoginFail() throws Exception{
    ResultActions resultActions = mockMvc.perform(formLogin("/login")
            .user("email", user.getEmail())
            .password("password", "wrong"))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/login?error"))
        .andDo(print());

    HttpSession session = resultActions.andReturn().getRequest().getSession();
    assertNotNull(session);
  }
}
