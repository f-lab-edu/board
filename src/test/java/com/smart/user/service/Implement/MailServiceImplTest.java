package com.smart.user.service.Implement;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Assertions;
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

  @Test
  public void 메일전송(){
    ArgumentCaptor<SimpleMailMessage> valueCapture = ArgumentCaptor.forClass(SimpleMailMessage.class);
    doNothing().when(javaMailSender).send(valueCapture.capture());

    mailService.sendAuthMail("test@test.com");

    verify(javaMailSender).send(any(SimpleMailMessage.class));
    Assertions.assertEquals("test@test.com", valueCapture.getValue().getTo()[0]);
  }

}