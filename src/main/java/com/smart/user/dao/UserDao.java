package com.smart.user.dao;

import com.smart.user.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {

  User getUserByNickname(String nickname);

  void deleteUserByNickname(String nickname);

  void joinUser(User user);

  boolean checkUserMail(String email);

  boolean checkUserNickname(String nickname);
}