package com.smart.mail.service.implement;

import com.smart.mail.service.MailService;
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
  public void sendAuthMail(String email, String authCode) {
    javaMailSender.send(makeAuthMail(email, authCode));
  }

  private SimpleMailMessage makeAuthMail(String email, String authCode) {
    SimpleMailMessage mailMessage = new SimpleMailMessage();

    mailMessage.setTo(email);
    mailMessage.setSubject("회원가입 인증 메일입니다.");
    StringBuilder sb = new StringBuilder();
    sb.append("[회원가입 인증]\n");
    sb.append("아래 링크를 클릭하시면 회원가입 인증이 완료됩니다\n");
    sb.append("http://localhost:8080/api/v1/user/join-auth?");
    sb.append("email=" + email);
    sb.append("&authCode=" + authCode);
    mailMessage.setText(sb.toString());

    return mailMessage;
  }

}
