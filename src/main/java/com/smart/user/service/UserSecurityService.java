package com.smart.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserSecurityService implements UserDetailsService {
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    com.smart.user.domain.User user = userService.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("nnn"));

    return org.springframework.security.core.userdetails.User.builder()
        .username(user.getName())
        .password(passwordEncoder.encode(user.getPassword()))
        .roles(user.getRole())
        .build();
  }

}