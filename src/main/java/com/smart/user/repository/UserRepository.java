package com.smart.user.repository;

import com.smart.user.domain.User;
import java.util.Optional;

public interface UserRepository {

  Optional<User> findByUserId(Long userId);

  Long save(User user);

}
