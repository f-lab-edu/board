package com.smart.user.service.Implement;

import com.smart.global.error.DuplicatedUserEmailException;
import com.smart.global.error.DuplicatedUserNicknameException;
import com.smart.user.controller.dto.UserDto.JoinRequest;
import com.smart.user.dao.UserDao;
import com.smart.user.domain.User;
import com.smart.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserDao userDao;

  public UserServiceImpl(UserDao userDao) {
    this.userDao = userDao;
  }

  @Override
  public void join(JoinRequest request) {
    if(userDao.checkUserMail(request.getEmail())) throw new DuplicatedUserEmailException();
    if(userDao.checkUserNickname(request.getNickname())) throw new DuplicatedUserNicknameException();
    userDao.joinUser(request.toEntity());
  }

  @Override
  public User getUser(String name) {
    return userDao.getUser(name);
  }
}
