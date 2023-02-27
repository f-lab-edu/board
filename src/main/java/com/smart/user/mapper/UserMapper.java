package com.smart.user.mapper;

import com.smart.user.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

  void joinUser(User user);
  boolean checkUserMail(String email);
  boolean checkUserNickname(String nickname);
}
