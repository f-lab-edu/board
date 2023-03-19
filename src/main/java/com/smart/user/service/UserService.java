package com.smart.user.service;

import com.smart.user.controller.dto.UserDto;

public interface UserService {

  void join(UserDto.JoinRequest request);

  String makeAuthCode();

  void saveAuthCode(String email, String code);

  UserDto.Response getUserByEmail(String email);

}
