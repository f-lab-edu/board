package com.smart.user.service;

import com.smart.user.controller.dto.UserDto.JoinRequest;
import com.smart.user.controller.dto.UserDto.UserResponse;

public interface UserService {

  void join(JoinRequest request);

  UserResponse getUser(String name);

}
