package com.smart.user.service;

import com.smart.global.error.NotFoundUserException;
import com.smart.user.dao.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSecurityService implements UserDetailsService {
  private final UserDao userDao;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    com.smart.user.domain.User user = userDao.getUserByEmail(email);
    if (user == null) {
      throw new NotFoundUserException();
    }

    return buildUserDetails(user);
  }

  private UserDetails buildUserDetails(com.smart.user.domain.User user) {
    return User.builder()
        .username(user.getName())
        .password(passwordEncoder.encode(user.getPassword()))
        .roles(user.getRole())
        .build();
  }
}
