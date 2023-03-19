package com.smart.user.service;

import com.smart.user.controller.dto.UserDto;

public interface UserService {

  void join(UserDto.JoinRequest request);

  String makeAuthCode();

  void saveAuthCode(String email, String authCode);

  void verifyAuthCode(String email, String authCode);

  UserDto.Response getUserByEmail(String email);

}
