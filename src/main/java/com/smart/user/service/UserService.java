package com.smart.user.service;

import com.smart.user.controller.dto.UserDto;

public interface UserService {

  void join(UserDto.JoinRequest request);

  UserDto.Response getUserByEmail(String email);

}
