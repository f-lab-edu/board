package com.smart.user.service.Implement;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.smart.global.error.DuplicatedUserEmailException;
import com.smart.global.error.DuplicatedUserNicknameException;
import com.smart.global.error.IllegalAuthCodeException;
import com.smart.global.error.NotFoundUserException;
import com.smart.mail.event.MailEvent;
import com.smart.user.controller.dto.UserDto.JoinRequest;
import com.smart.user.controller.dto.UserDto.Response;
import com.smart.user.dao.UserDao;
import com.smart.user.domain.Status;
import com.smart.user.domain.User;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mock.web.MockHttpSession;

/**
 * Mockito ArgumentCaptor : ArgumentCaptor를 사용하여 메서드에 전달된 파라미터를 캡처하여 테스트 할 수 있다.
 * Mockito Verify : 메서드 호출, 호출 횟수, interaction 등을 검증할 수 있다.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  UserDao userDao;

  HttpSession httpSession;

  ApplicationEventPublisher eventPublisher;

  UserServiceImpl userService;

  JoinRequest joinRequest;

  User user;

  @BeforeEach
  public void mock주입() {
    userDao = mock(UserDao.class);
    httpSession = new MockHttpSession();
    eventPublisher = mock(ApplicationEventPublisher.class);
    userService = new UserServiceImpl(userDao, eventPublisher, httpSession);
  }

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

  @DisplayName("회원가입을 성공하여 인증메일이벤트를 호출한다.")
  @Test
  public void 인증메일이벤트호출() {
    ArgumentCaptor<MailEvent> mailEventCaptor = ArgumentCaptor.forClass(MailEvent.class);
    doNothing().when(eventPublisher).publishEvent(mailEventCaptor.capture());

    userService.join(joinRequest);

    verify(eventPublisher).publishEvent(any(MailEvent.class));
    Assertions.assertEquals(joinRequest.getEmail(), mailEventCaptor.getValue().getEmail());
  }

  @DisplayName("회원가입을 성공하여 인증코드를 세션에 저장한다.")
  @Test
  public void 인증코드세션저장() {
    userService.join(joinRequest);

    Assertions.assertNotNull(httpSession.getAttribute(joinRequest.getEmail()));
  }

  @DisplayName("회원가입을 성공하여 회원정보를 DB에 저장한다.")
  @Test
  public void 회원정보저장() {
    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    doNothing().when(userDao).joinUser(userCaptor.capture());

    userService.join(joinRequest);

    verify(userDao).joinUser(any(User.class));
    Assertions.assertEquals(joinRequest.getEmail(), userCaptor.getValue().getEmail());
  }

  @DisplayName("중복닉네임으로 회원가입을 실패한다.")
  @Test
  public void 중복닉네임회원가입실패() {
    when(userDao.checkUserNickname("nickname")).thenReturn(true);

    Assertions
        .assertThrows(DuplicatedUserNicknameException.class, () -> userService.join(joinRequest));
    verify(userDao).checkUserNickname("nickname");
  }

  @DisplayName("중복이메일로 회원가입을 실패한다.")
  @Test
  public void 중복이메일회원가입실패() {
    when(userDao.checkUserMail("test@email")).thenReturn(true);

    Assertions
        .assertThrows(DuplicatedUserEmailException.class, () -> userService.join(joinRequest));
    verify(userDao).checkUserMail("test@email");
  }

  @DisplayName("올바른 인증요청으로 유저상태가 NORMAL로 업데이트된다.")
  @Test
  public void 유저상태업데이트() {
    httpSession.setAttribute("test@email", "authCode");
    doNothing().when(userDao).updateUserStatus("test@email", Status.NORMAL);

    userService.verifyAuthCode("test@email", "authCode");

    verify(userDao).updateUserStatus("test@email", Status.NORMAL);
  }

  @DisplayName("올바르지 않은 인증요청으로 유저상태 업데이트를 실패한다.")
  @Test
  public void 유저상태업데이트실패() {
    httpSession.setAttribute("test@email", "unAuthCode");

    Assertions
        .assertThrows(IllegalAuthCodeException.class,
            () -> userService.verifyAuthCode("test@email", "authCode"));
  }

  @DisplayName("가입된 이메일로 유저찾기를 성공한다.")
  @Test
  public void 유저찾기성공() {
    when(userDao.getUserByEmail("test@email")).thenReturn(user);

    Response response = userService.getUserByEmail("test@email");

    verify(userDao).getUserByEmail("test@email");
    Assertions.assertEquals("test@email", response.getEmail());
  }

  @DisplayName("가입되지 않은 이메일로 유저찾기를 실패한다.")
  @Test
  public void 유저찾기실패() {
    when(userDao.getUserByEmail("test@email")).thenReturn(null);

    Assertions
        .assertThrows(NotFoundUserException.class, () -> userService.getUserByEmail("test@email"));
    verify(userDao).getUserByEmail("test@email");
  }

}