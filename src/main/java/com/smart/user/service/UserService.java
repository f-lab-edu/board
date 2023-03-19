package com.smart.user.service;

import com.smart.user.controller.dto.UserDto;
import com.smart.user.domain.User;
import java.util.Optional;

public interface UserService {

  void join(UserDto.JoinRequest request);

  UserDto.Response getUserByEmail(String email);

  Optional<User> findByEmail(String username);
}
