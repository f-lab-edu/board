package com.smart.user.service;

import com.smart.user.controller.dto.UserDto.JoinRequest;

public interface UserService {

  void join(JoinRequest request);

}
