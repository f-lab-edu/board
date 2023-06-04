package com.smart.user.repository;

import com.smart.user.domain.User;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
        .password("Password1*")
        .email("test@email")
        .build();
  }

  @AfterEach
  public void 테스트유저삭제() {
    userRepository.deleteByEmail(user.getEmail());
  }

  @DisplayName("회원가입 성공 테스트")
  @Test
  public void 유저회원가입() {
    userRepository.save(user);
    User foundUser = userRepository.findByEmail(user.getEmail());
    Assertions.assertEquals(foundUser, user);
  }

  @DisplayName("중복된 이메일이 존재하는 경우 true 를 반환한다.")
  @Test
  public void 중복된이메일체크() {
    userRepository.save(user);

    boolean checkUserMail = userRepository.existsByEmail("test@email");
    Assertions.assertTrue(checkUserMail);
  }

  @DisplayName("중복된 이메일이 존재하지 않는 경우 false를 반환한다.")
  @Test
  public void 중복되지않는이메일체크(){
    userRepository.save(user);

    boolean checkUserMail = userRepository.existsByEmail("not-in@email");
    Assertions.assertFalse(checkUserMail);
  }

  @DisplayName("중복된 닉네임이 존재할 경우 true 를 반환한다.")
  @Test
  public void 중복된닉네임체크() {
    userRepository.save(user);

    boolean checkUserNickname = userRepository.existsByNickname("nickname");
    Assertions.assertTrue(checkUserNickname);
  }

  @DisplayName("중복된 닉네임이 존재하지 않을 경우 false 를 반환한다.")
  @Test
  public void 중복되지않는닉네임체크() {
    userRepository.save(user);

    boolean checkUserNickname = userRepository.existsByNickname("not-in");
    Assertions.assertFalse(checkUserNickname);
  }

}
