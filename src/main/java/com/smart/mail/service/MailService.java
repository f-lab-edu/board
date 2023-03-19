package com.smart.mail.service;

import org.springframework.mail.SimpleMailMessage;

public interface MailService {

  void sendAuthMail(String email, String authCode);

  SimpleMailMessage makeAuthMail(String email, String code);

}
