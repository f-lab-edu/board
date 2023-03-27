package com.smart.mail.event;

import lombok.Getter;

@Getter
public class MailAuthEvent {

  private String email;

  private String authCode;

  public MailAuthEvent(String email, String authCode) {
    this.email = email;
    this.authCode = authCode;
  }
}
