package com.smart.mail.service;

public interface MailService {

  void sendAuthMail(String email, String authCode);

  void sendPasswordResetEmail(String email, String temporaryPassword);

}
