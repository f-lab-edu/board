package com.smart.mail.service.implement;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class MailServiceImplTest {

  @Mock
  JavaMailSender javaMailSender;

  @InjectMocks
  MailServiceImpl mailService;

  final String MAIL = "test@test.com";
  final String AUTH_CODE = "authCode";

  @DisplayName("회원가입 인증 메일을 전송한다.")
  @Test
  public void 인증메일전송() {
    ArgumentCaptor<SimpleMailMessage> valueCapture = ArgumentCaptor
        .forClass(SimpleMailMessage.class);
    doNothing().when(javaMailSender).send(valueCapture.capture());

    mailService.sendAuthMail(MAIL, AUTH_CODE);

    verify(javaMailSender).send(any(SimpleMailMessage.class));
    Assertions.assertEquals(MAIL, valueCapture.getValue().getTo()[0]);
  }
}