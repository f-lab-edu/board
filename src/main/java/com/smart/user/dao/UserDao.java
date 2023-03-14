package com.smart.user.dao;

import com.smart.user.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
  User getUser(String name);
}
