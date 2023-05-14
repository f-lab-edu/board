package com.smart.user.repository;

import com.smart.global.error.NotFoundUserException;
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
  public User findByEmail(String email) {
    return users.stream()
        .filter(user -> user.getEmail().equals(email))
        .findFirst()
        .orElseThrow(NotFoundUserException::new);
  }

  @Override
  public void deleteByEmail(String email) {
    User user = this.findByEmail(email);
    users.remove(user);
  }

  @Override
  public Long save(User user) {
    if (user.getUserId() == null) {
      user.updateUserId(sequence++);
    }
    users.removeIf(existingUser -> existingUser.getUserId().equals(user.getUserId()));
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
    return users.stream()
        .filter(user -> user.getUserId().equals(userId))
        .findFirst();
  }
}
