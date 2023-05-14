package com.smart.user.repository;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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

  @Override
  public String generateAuthCode() {
    return generateRandomString(10);
  }

  public String generateRandomString(int len) {
    final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    SecureRandom random = new SecureRandom();

    return IntStream.range(0, len)
        .map(i -> random.nextInt(chars.length()))
        .mapToObj(randomIndex -> String.valueOf(chars.charAt(randomIndex)))
        .collect(Collectors.joining());
  }
}
