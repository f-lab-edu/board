package com.smart.user.service.Implement;

import com.smart.user.service.MailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * JavaMailSender : 스프링의 MailSender를 확장한 인터페이스이며 구현체는 JavaMailSenderImpl이다.
 */
@Service
public class MailServiceImpl implements MailService {

  private final JavaMailSender javaMailSender;

  public MailServiceImpl(JavaMailSender javaMailSender) {
    this.javaMailSender = javaMailSender;
  }

  @Override
  public void sendAuthMail(String email) {
    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setTo(email);
    simpleMailMessage.setSubject("[test]테스트 메일입니다.");
    simpleMailMessage.setText("테스트 메일입니다.\n");
    javaMailSender.send(simpleMailMessage);
  }

}
