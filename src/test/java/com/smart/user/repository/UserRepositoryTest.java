package com.smart.user.repository;

import com.smart.user.domain.User;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserRepositoryTest {

  private User user;

  private final UserRepositoryImpl userRepository = new UserRepositoryImpl();

  @BeforeEach
  public void 테스트유저생성() {
    user = User
        .builder()
        .userId(1L)
        .name("name")
        .nickname("nickname")
        .password("password")
        .email("test@email")
        .build();
  }

  @AfterEach
  public void 테스트유저삭제() {
    userRepository.deleteByEmail(user.getEmail());
  }

  @Test
  public void 유저회원가입() {
    userRepository.save(user);
    Optional<User> foundUser = userRepository.findByEmail(user.getEmail());
    Assertions.assertTrue(foundUser.isPresent());
  }

  @Test
  public void 중복이메일체크() {
    userRepository.save(user);

    boolean checkUserMail = userRepository.existsByEmail("test@email");
    Assertions.assertTrue(checkUserMail);

    checkUserMail = userRepository.existsByEmail("not-in@email");
    Assertions.assertFalse(checkUserMail);
  }

  @Test
  public void 중복닉네임체크() {
    userRepository.save(user);

    boolean checkUserNickname = userRepository.existsByNickname("nickname");
    Assertions.assertTrue(checkUserNickname);

    checkUserNickname = userRepository.existsByNickname("not-in");
    Assertions.assertFalse(checkUserNickname);
  }

}