package com.smart.user.service.Implement;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.smart.global.error.DuplicatedUserEmailException;
import com.smart.global.error.DuplicatedUserNicknameException;
import com.smart.global.error.NotFoundUserException;
import com.smart.user.controller.dto.UserDto.JoinRequest;
import com.smart.user.controller.dto.UserDto.Response;
import com.smart.user.dao.UserDao;
import com.smart.user.domain.Status;
import com.smart.user.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Mockito ArgumentCaptor : ArgumentCaptor를 사용하여 메서드에 전달된 파라미터를 캡처하여 테스트 할 수 있다. Mockito Verify :
 * 메서드 호출, 호출 횟수, interaction 등을 검증할 수 있다.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock
  UserDao userDao;

  @InjectMocks
  UserServiceImpl userService;

  JoinRequest joinRequest;

  User user;

  @BeforeEach
  public void 테스트유저생성() {
    joinRequest = JoinRequest
        .builder()
        .name("name")
        .nickname("nickname")
        .password("password")
        .email("test@email")
        .build();

    user = User
        .builder()
        .userId(1L)
        .name("name")
        .nickname("nickname")
        .password("password")
        .email("test@email")
        .userStatus(Status.NORMAL)
        .build();
  }

  @Test
  public void 회원가입성공() {
    ArgumentCaptor<User> valueCapture = ArgumentCaptor.forClass(User.class);
    doNothing().when(userDao).joinUser(valueCapture.capture());

    userService.join(joinRequest);

    verify(userDao).joinUser(any(User.class));
    Assertions.assertEquals(joinRequest.getEmail(), valueCapture.getValue().getEmail());
    Assertions.assertEquals(joinRequest.getPassword(), valueCapture.getValue().getPassword());
  }

  @Test
  public void 중복닉네임으로인한_회원가입_실패() {
    when(userDao.checkUserNickname("nickname")).thenReturn(true);

    Assertions
        .assertThrows(DuplicatedUserNicknameException.class, () -> userService.join(joinRequest));
    verify(userDao).checkUserNickname("nickname");
  }

  @Test
  public void 중복이메일로인한_회원가입_실패() {
    when(userDao.checkUserMail("test@email")).thenReturn(true);

    Assertions
        .assertThrows(DuplicatedUserEmailException.class, () -> userService.join(joinRequest));
    verify(userDao).checkUserMail("test@email");
  }

  @Test
  public void 이메일로_유저찾기성공() {
    when(userDao.getUserByEmail("test@email")).thenReturn(user);

    Response response = userService.getUserByEmail("test@email");

    Assertions.assertEquals("test@email", response.getEmail());
    verify(userDao).getUserByEmail("test@email");
  }

  @Test
  public void 이메일로_유저찾기실패() {
    when(userDao.getUserByEmail("test@email")).thenReturn(null);

    Assertions
        .assertThrows(NotFoundUserException.class, () -> userService.getUserByEmail("test@email"));
    verify(userDao).getUserByEmail("test@email");
  }

}