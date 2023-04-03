package com.smart.user.dao;

import com.smart.user.domain.Status;
import com.smart.user.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserDao {

  User getUserByEmail(String email);

  void deleteUserByEmail(String email);

  void joinUser(User user);

  boolean checkUserMail(String email);

  boolean checkUserNickname(String nickname);

  void updateUserStatus(@Param("email")String email, @Param("userStatus") Status userStatus);
}
