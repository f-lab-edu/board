package com.smart.user.service;

import com.smart.global.error.NotFoundUserException;
import com.smart.user.controller.dto.UserDto;
import com.smart.user.controller.dto.UserDto.UserInfo;
import com.smart.user.dao.UserDao;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
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
    var userInfo= UserInfo.builder()
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
    assertEquals(email,userDetails.getUsername());
  }
}

