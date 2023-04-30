package com.smart.user.service;

import com.smart.user.controller.dto.UserDto;
import com.smart.user.controller.dto.UserDto.UserInfo;
import com.smart.user.domain.User;

public interface UserService {

  User join(UserDto.JoinRequest request);

  void verifyAuthCode(String email, String authCode);

  UserInfo getUserByEmail(String email);

  void updateUserInfo(User user);

  void isDuplicateNickname(String nickname);
}
