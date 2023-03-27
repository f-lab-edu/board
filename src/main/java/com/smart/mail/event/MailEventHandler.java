package com.smart.mail.event;

import com.smart.mail.service.MailService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MailEventHandler{

  private final MailService mailService;

  public MailEventHandler(MailService mailService) {
    this.mailService = mailService;
  }

  @EventListener
  @Async
  public void eventHandle(MailAuthEvent event) {
    mailService.sendAuthMail(event.getEmail(), event.getAuthCode());
  }
}