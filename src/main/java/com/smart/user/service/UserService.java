package com.smart.user.service;

import com.smart.user.controller.dto.UserDto;
import com.smart.user.controller.dto.UserDto.UserInfo;
import java.util.Optional;

public interface UserService {

  void join(UserDto.JoinRequest request);

  void verifyAuthCode(String email, String authCode);

  Optional<UserInfo> getUserByEmail(String email);


}
