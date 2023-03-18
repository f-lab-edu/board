package com.smart.user.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MailServiceTest {

  @Autowired
  private MailService mailService;

  @DisplayName("실제 메일 발송 여부를 테스트한다.")
  @Test
  public void 메일전송() {
    mailService.sendAuthMail("gjwjdghk123@naver.com");
  }

}