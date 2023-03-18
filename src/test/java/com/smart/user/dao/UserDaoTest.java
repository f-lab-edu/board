package com.smart.user.dao;

import com.smart.user.controller.dto.UserDto.JoinRequest;
import com.smart.user.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

/**
 * @MybatisTest : MyBatis컴포넌트(MapperInterface, SqlSession)를 테스트하기 위해 사용하는 애노테이션
 * - 기본 MyBatis Configure를 해주며, MapperInterface 및 내장 DB를 구성한다.
 * @AutoConfigureTestDatabase : 실제 DB 연결을 위한 애노테이션
 */
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserDaoTest {

  @Autowired
  private UserDao userDao;

  User user;

  @BeforeEach
  public void 테스트유저생성() {
    userDao.deleteUserByEmail("test@email");
    user = JoinRequest
        .builder()
        .name("name")
        .nickname("nickname")
        .password("password")
        .email("test@email")
        .build()
        .toEntity();
  }

  @AfterEach
  public void 테스트유저삭제(){
    userDao.deleteUserByEmail("test@email");
  }

  @Test
  public void 유저회원가입() {
    userDao.joinUser(user);

    User retUser = userDao.getUserByEmail("test@email");
    Assertions.assertEquals(user.getUserId(), retUser.getUserId());
  }

  @Test
  public void 중복이메일체크() {
    userDao.joinUser(user);

    boolean checkUserMail = userDao.checkUserMail("test@email");
    Assertions.assertEquals(checkUserMail, true);

    checkUserMail = userDao.checkUserMail("not-in@email");
    Assertions.assertEquals(checkUserMail, false);
  }

  @Test
  public void 중복닉네임체크() {
    userDao.joinUser(user);

    boolean checkUserNickname = userDao.checkUserNickname("nickname");
    Assertions.assertEquals(checkUserNickname, true);

    checkUserNickname = userDao.checkUserNickname("not-in");
    Assertions.assertEquals(checkUserNickname, false);
  }

}