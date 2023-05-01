package com.smart.user.repository;

import com.smart.user.domain.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

  private final List<User> users = new ArrayList<>();

  private static Long sequence = 1L;

  @Override
  public Optional<User> findByEmail(String email) {
    return users.stream()
        .filter(user -> user.getEmail().equals(email)).findFirst();
  }

  @Override
  public void deleteByEmail(String email) {
    this.findByEmail(email).ifPresent(users::remove);
  }

  @Override
  public Long save(User user) {
    if (user.getUserId() == null) {
      user.setUserId(sequence++);
    }
    users.add(user);
    return user.getUserId();
  }

  @Override
  public boolean existsByEmail(String email) {
    return users.stream()
        .anyMatch(user -> user.getEmail().equals(email));
  }

  @Override
  public boolean existsByNickname(String nickname) {
    return users.stream()
        .anyMatch(user -> user.getNickname().equals(nickname));
  }
}
