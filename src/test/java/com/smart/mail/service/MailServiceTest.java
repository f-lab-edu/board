package com.smart.mail.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSendException;

@SpringBootTest
class MailServiceTest {

  @Autowired
  private MailService mailService;

  final String MAIL = "gjwjdghk123@naver.com";
  final String AUTH_CODE = "authCode";

  @DisplayName("메일 발송에 성공하여 실제로 발송됩니다.")
  @Test
  public void 메일전송성공() {
    mailService.sendAuthMail(MAIL, AUTH_CODE);
  }

  @DisplayName("이메일이 Null일 경우 발송에 실패합니다.")
  @Test
  public void 메일전송실패() {
    Assertions
        .assertThrows(NullPointerException.class, () -> mailService.sendAuthMail(null, AUTH_CODE));
  }

  @DisplayName("올바른 이메일이 아닐 경우 발송에 실패합니다.")
  @Test
  public void 메일전송실패2() {
    Assertions
        .assertThrows(MailSendException.class, () -> mailService.sendAuthMail("?", AUTH_CODE));
  }
}