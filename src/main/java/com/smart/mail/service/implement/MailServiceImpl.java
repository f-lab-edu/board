package com.smart.mail.service.implement;

import com.smart.mail.service.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * JavaMailSender : 스프링의 MailSender를 확장한 인터페이스이며 구현체는 JavaMailSenderImpl이다.
 */
@Service
public class MailServiceImpl implements MailService {

  private final JavaMailSender javaMailSender;

  @Value("${mail.auth.url}")
  private String mailUrl;

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
    String message = """
                  [회원가입 인증]\n
                  아래 링크를 클릭하시면 회원가입 인증이 완료됩니다.\n
                  %s?email=%s&authCode=%s
                  """.formatted(mailUrl, email, authCode);
    mailMessage.setText(message);

    return mailMessage;
  }

  @Override
  public void sendPasswordResetEmail(String email, String temporaryPassword) {
    javaMailSender.send(makePasswordResetMail(email, temporaryPassword));
  }

  private SimpleMailMessage makePasswordResetMail(String email, String temporaryPassword) {
    SimpleMailMessage mailMessage = new SimpleMailMessage();

    mailMessage.setTo(email);
    mailMessage.setSubject("임시 비밀번호가 발급되었습니다.");
    String message = """
                  [임시 비밀번호]\n
                  아래 임시 비밀번호로 로그인 후 비밀번호를 재설정해주세요.\n
                  [%s]
                  """.formatted(temporaryPassword);
    mailMessage.setText(message);
    return mailMessage;
  }
}
