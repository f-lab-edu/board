package com.smart.user.service;

import com.smart.user.dao.UserDao;
import com.smart.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserDao userDao;

  @Override
  public User getUser(String name) {
    return userDao.getUser(name);
  }
}
