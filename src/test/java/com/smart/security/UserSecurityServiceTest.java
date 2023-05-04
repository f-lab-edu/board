package com.smart.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.smart.user.controller.dto.UserDto.UserInfo;
import com.smart.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserSecurityServiceTest {

  @InjectMocks
  private UserSecurityService userSecurityService;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private UserService userService;


  @Test
  public void loadUserByUsername() {
    //given : 이메일이 주어지고
    var email = "test@gmail.com";
    var userInfo = UserInfo.builder()
        .userId(1L)
        .name("test")
        .email(email)
        .password("1234")
        .role("USER")
        .build();

    //when : 사용자를 조회했을때
    when(userService.getUserByEmail(email)).thenReturn(userInfo);
    when(passwordEncoder.encode(userInfo.getPassword())).thenReturn(userInfo.getPassword());
    var userDetails = userSecurityService.loadUserByUsername(email);

    //then : 사용자 정보를 확인할 수 있다.
    assertEquals(email, userDetails.getUsername());
  }
}

