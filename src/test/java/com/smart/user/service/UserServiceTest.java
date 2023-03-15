package com.smart.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.smart.global.error.DuplicatedUserEmailException;
import com.smart.user.controller.dto.UserDto.JoinRequest;
import com.smart.user.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  private JoinRequest joinRequest;

  @Mock
  UserMapper userMapper;

  @InjectMocks
  UserService userService;

  @BeforeEach
  @DisplayName("User를 생성한다.")
  public void createUser() {

    joinRequest = JoinRequest.builder()
        .name("test_name")
        .nickname("test_nickname")
        .email("test@email.com")
        .password("password")
        .build();
  }

  @Test
  @DisplayName("User메일 중복으로 회원가입을 실패한다.")
  public void duplicatedEmail() {
    when(userMapper.checkUserMail(joinRequest.getEmail())).thenReturn(true);

    DuplicatedUserEmailException exception = assertThrows(
        DuplicatedUserEmailException.class, () -> userService.join(joinRequest));

    assertEquals(exception.getMessage(), "이메일이 중복됩니다.");
  }

}