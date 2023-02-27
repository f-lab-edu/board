package com.smart.user.service.Implement;

import com.smart.global.error.DuplicatedUserEmailException;
import com.smart.global.error.DuplicatedUserNicknameException;
import com.smart.user.controller.dto.UserDto.JoinRequest;
import com.smart.user.mapper.UserMapper;
import com.smart.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserMapper userMapper;

  public UserServiceImpl(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  @Override
  public void join(JoinRequest request) {
    if(userMapper.checkUserMail(request.getEmail())) throw new DuplicatedUserEmailException();
    if(userMapper.checkUserNickname(request.getNickname())) throw new DuplicatedUserNicknameException();
    userMapper.joinUser(request.toEntity());
  }
}
