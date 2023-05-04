package com.smart.user.repository;

import com.smart.user.domain.User;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

  private Map<Long, User> users = new HashMap<>();
  private static Long sequence = 0L;

  @Override
  public Optional<User> findByUserId(Long userId) {
    return Optional.ofNullable(users.get(userId));
  }

  @Override
  public Long save(User user) {
    user.setUserId(sequence++);
    users.put(user.getUserId(), user);
    return user.getUserId();
  }

}
