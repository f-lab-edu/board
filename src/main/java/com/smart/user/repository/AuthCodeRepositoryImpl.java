package com.smart.user.repository;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class AuthCodeRepositoryImpl implements AuthCodeRepository {

  private final Map<String, String> authCodeMap = new HashMap<>();

  @Override
  public void saveAuthCode(String email, String authCode) {
    authCodeMap.put(email, authCode);
  }

  @Override
  public String getAuthCode(String email) {
    return authCodeMap.get(email);
  }

  @Override
  public void removeAuthCode(String email) {
    authCodeMap.remove(email);
  }
}
