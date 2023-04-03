package com.smart.mail.event;

import com.smart.mail.service.MailService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class MailEventHandler{

  private final MailService mailService;

  public MailEventHandler(MailService mailService) {
    this.mailService = mailService;
  }

  // 회원가입 트랜잭션과 분리하기 위해 Transaction을 새로 만들어줌.
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @TransactionalEventListener
  // 성능을 위해 비동기로 처리한다(회원가입 스레드와 메일전송 스레드 분리)
  @Async
  public void eventHandle(MailAuthEvent event) {
    mailService.sendAuthMail(event.getEmail(), event.getAuthCode());
  }
}