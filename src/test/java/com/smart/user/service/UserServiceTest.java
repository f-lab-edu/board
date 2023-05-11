package com.smart.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.smart.global.error.DuplicatedUserEmailException;
import com.smart.global.error.DuplicatedUserNicknameException;
import com.smart.global.error.IllegalAuthCodeException;
import com.smart.global.error.InvalidUserInfoException;
import com.smart.global.error.NotFoundUserException;
import com.smart.mail.event.MailAuthEvent;
import com.smart.mail.service.MailService;
import com.smart.mail.service.implement.MailServiceImpl;
import com.smart.user.controller.dto.UserInfoDto;
import com.smart.user.controller.dto.UserSaveDto;
import com.smart.user.controller.dto.UserUpdateDto;
import com.smart.user.domain.Status;
import com.smart.user.repository.AuthCodeRepository;
import com.smart.user.repository.AuthCodeRepositoryImpl;
import com.smart.user.repository.UserRepository;
import com.smart.user.repository.UserRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.javamail.JavaMailSender;

class UserServiceTest {

  private final UserRepository userRepository = new UserRepositoryImpl();
  private final AuthCodeRepository authCodeRepository = new AuthCodeRepositoryImpl();

  private final ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);

  private final JavaMailSender javaMailSender = mock(JavaMailSender.class);
  private final MailService mailService = new MailServiceImpl(javaMailSender);

  private final UserService userService = new UserService(userRepository, eventPublisher,
      authCodeRepository, mailService);

  private UserSaveDto userSaveDto;

  @BeforeEach
  public void 테스트유저생성() {
    userSaveDto = UserSaveDto
        .builder()
        .name("name")
        .nickname("nickname")
        .password("Password1*")
        .email("test@email")
        .build();

    String authCode = "123456";
    authCodeRepository.saveAuthCode(userSaveDto.getEmail(), authCode);
  }

  @DisplayName("회원가입 성공 테스트")
  @Test
  public void 회원가입성공() {
    userService.join(userSaveDto);

    UserInfoDto savedUser = userService.getUserByEmail(userSaveDto.getEmail());
    assertEquals(userSaveDto.getEmail(), savedUser.getEmail());
    assertEquals(userSaveDto.getNickname(), savedUser.getNickname());
  }

  @DisplayName("회원가입을 성공하여 인증메일이벤트를 호출한다.")
  @Test
  public void 인증메일이벤트호출() {
    ArgumentCaptor<MailAuthEvent> mailEventCaptor = ArgumentCaptor.forClass(MailAuthEvent.class);
    doNothing().when(eventPublisher).publishEvent(mailEventCaptor.capture());

    userService.join(userSaveDto);

    verify(eventPublisher).publishEvent(any(MailAuthEvent.class));
    Assertions.assertEquals(userSaveDto.getEmail(), mailEventCaptor.getValue().getEmail());
  }

  @DisplayName("중복닉네임으로 회원가입을 실패한다.")
  @Test
  public void 중복닉네임회원가입실패() {
    UserSaveDto userSaveDto1 = UserSaveDto
        .builder()
        .name("name")
        .nickname("nickname")
        .password("Password1*")
        .email("test1@email")
        .build();
    userService.join(userSaveDto1);

    assertThrows(DuplicatedUserNicknameException.class, () -> userService.join(userSaveDto));
  }

  @DisplayName("중복이메일로 회원가입을 실패한다.")
  @Test
  public void 중복이메일회원가입실패() {
    userService.join(userSaveDto);

    assertThrows(DuplicatedUserEmailException.class, () -> userService.join(userSaveDto));
  }

  @DisplayName("올바른 인증요청으로 유저상태가 NORMAL로 업데이트된다.")
  @Test
  public void 유저상태업데이트() {
    userService.join(userSaveDto);

    userService.verifyAuthCode(userSaveDto.getEmail(), "123456");

    UserInfoDto retUserInfo = userService.getUserByEmail(userSaveDto.getEmail());
    assertEquals(retUserInfo.getUserStatus(), Status.NORMAL);
  }

  @DisplayName("올바르지 않은 인증요청으로 유저상태 업데이트를 실패한다.")
  @Test
  public void 유저상태업데이트실패() {
    userService.join(userSaveDto);

    assertThrows(IllegalAuthCodeException.class,
        () -> userService.verifyAuthCode(userSaveDto.getEmail(), "authCode"));
  }

  @DisplayName("가입된 이메일로 유저찾기를 성공한다.")
  @Test
  public void 유저찾기성공() {
    userService.join(userSaveDto);
    UserInfoDto userInfo = userService.getUserByEmail("test@email");

    Assertions.assertEquals("test@email", userInfo.getEmail());
  }

  @DisplayName("가입되지 않은 이메일로 유저찾기를 실패한다.")
  @Test
  public void 유저찾기실패() {
    assertThrows(NotFoundUserException.class, () -> userService.getUserByEmail("test@email"));
  }

  @DisplayName("닉네임 변경을 성공한다.")
  @Test
  public void 유저정보변경성공() {
    // Given
    userService.join(userSaveDto);
    String newNickname = "newNickname";
    UserUpdateDto userUpdateDto = UserUpdateDto
        .builder()
        .name("name")
        .nickname(newNickname)
        .password("Password1*")
        .email("test@email")
        .build();

    // When
    userService.updateUserInfo(userUpdateDto);

    UserInfoDto retUserInfo = userService.getUserByEmail(userUpdateDto.getEmail());

    //then
    assertEquals(retUserInfo.getNickname(), newNickname);

  }

  @DisplayName("유효하지않은 닉네임을 입력한 경우 InvalidUserInfoException이 발생한다.")
  @Test
  void 유효하지않은닉네임변경() {
    // Given
    userService.join(userSaveDto);
    String wrongNickname = "invalidTooLongNickname";
    UserUpdateDto userUpdateDto = UserUpdateDto
        .builder()
        .name("name")
        .nickname(wrongNickname)
        .password("Password1*")
        .email("test@email")
        .build();

    assertThrows(InvalidUserInfoException.class, () -> userService.updateUserInfo(userUpdateDto));
  }

  @DisplayName("비밀번호를 잊은 사용자가 임시 비밀번호를 발급한다.")
  @Test
  void 임시비밀번호발급성공() {
    userService.join(userSaveDto);

    userService.resetPassword(userSaveDto.getEmail());

    UserInfoDto retUserInfo = userService.getUserByEmail(userSaveDto.getEmail());
    String originalPassword = userSaveDto.getPassword();
    String newPassword = retUserInfo.getPassword();
    assertNotEquals(newPassword, originalPassword);
  }

  @DisplayName("유효하지 않은 비밀번호를 입력한 경우 InvalidUserInfoException이 발생한다.")
  @Test
  void 유효하지않은비밀번호변경(){
    userService.join(userSaveDto);

    String invalidPassword = "invalidPassword";

    UserUpdateDto userUpdateDto = UserUpdateDto
        .builder()
        .name("name")
        .nickname("nickname")
        .password(invalidPassword)
        .email("test@email")
        .build();

    assertThrows(InvalidUserInfoException.class, () -> userService.updateUserInfo(userUpdateDto));
  }
}