package com.smart.user.repository;

import com.smart.user.domain.User;
import java.util.Optional;

public interface UserRepository {

  Optional<User> findByEmail(String email);

  void deleteByEmail(String email);

  Long save(User user);

  boolean existsByEmail(String email);

  boolean existsByNickname(String nickname);

  Optional<User> findByUserId(Long userId);
}
