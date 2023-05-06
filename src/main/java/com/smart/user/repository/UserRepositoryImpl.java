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
    for (User user : users) {
      if (user.getEmail().equals(email)) {
        return Optional.of(user);
      }
    }
    return Optional.empty();
  }

  @Override
  public void deleteByEmail(String email) {
    Optional<User> userOptional = findByEmail(email);
    userOptional.ifPresent(users::remove);
  }

  @Override
  public Long save(User user) {
    if (user.getUserId() == null) {
      user.updateUserId(sequence++);
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

  @Override
  public Optional<User> findByUserId(Long userId) {
    for (User user : users) {
      if (user.getUserId().equals(userId)) {
        return Optional.of(user);
      }
    }
    return Optional.empty();
  }
}
