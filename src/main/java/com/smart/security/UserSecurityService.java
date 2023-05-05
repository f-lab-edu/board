package com.smart.security;

import com.smart.user.controller.dto.UserInfoDto;
import com.smart.user.service.UserService;
import java.util.Arrays;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSecurityService implements UserDetailsService {

  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String email) {
    UserInfoDto userInfo = userService.getUserByEmail(email);

    return buildUserDetails(userInfo);
  }

  private CustomUserDetails buildUserDetails(UserInfoDto userInfo) {
    Collection<? extends GrantedAuthority> authorities = Arrays.asList(
        new SimpleGrantedAuthority(userInfo.getRole()));

    CustomUserDetails userDetails = new CustomUserDetails();
    userDetails.setUserId(userInfo.getUserId());
    userDetails.setName(userInfo.getName());
    userDetails.setNickname(userInfo.getNickname());
    userDetails.setEmail(userInfo.getEmail());
    userDetails.setPassword(passwordEncoder.encode(userInfo.getPassword()));
    userDetails.setAuthorities(authorities);

    return userDetails;
  }
}
