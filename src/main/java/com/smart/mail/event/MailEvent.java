package com.smart.mail.event;

import lombok.Getter;

@Getter
public class MailEvent {

  private String email;

  private String authCode;

  public MailEvent(String email, String authCode) {
    this.email = email;
    this.authCode = authCode;
  }
}
