package com.smart.user.repository;

public interface AuthCodeRepository {
  void saveAuthCode(String email, String authCode);

  String getAuthCode(String email);

  void removeAuthCode(String email);
}
