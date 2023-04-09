package com.smart.user.service;

import com.smart.global.error.NotFoundUserException;
import com.smart.user.dao.UserDao;
import com.smart.user.domain.CustomUserDetails;
import java.util.Arrays;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

  private CustomUserDetails buildUserDetails(com.smart.user.domain.User user) {
    Collection<? extends GrantedAuthority> authorities = Arrays.asList(
        new SimpleGrantedAuthority(user.getRole()));

    CustomUserDetails userDetails = new CustomUserDetails();
    userDetails.setUserId(user.getUserId());
    userDetails.setName(user.getName());
    userDetails.setNickname(user.getNickname());
    userDetails.setEmail(user.getEmail());
    userDetails.setPassword(passwordEncoder.encode(user.getPassword()));
    userDetails.setAuthorities(authorities);

    return userDetails;
  }
}
