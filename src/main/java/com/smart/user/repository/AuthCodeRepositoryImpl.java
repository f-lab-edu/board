package com.smart.user.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
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
    return getTempPassword();
  }
  public String getTempPassword(){
    char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
        'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

    String str = "";

    // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
    int idx = 0;
    for (int i = 0; i < 10; i++) {
      idx = (int) (charSet.length * Math.random());
      str += charSet[idx];
    }
    return str;
  }
}
