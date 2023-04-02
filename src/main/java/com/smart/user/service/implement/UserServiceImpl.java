package com.smart.user.service.implement;

import com.smart.global.error.DuplicatedUserEmailException;
import com.smart.global.error.DuplicatedUserNicknameException;
import com.smart.global.error.IllegalAuthCodeException;
import com.smart.global.error.NotFoundUserException;
import com.smart.mail.event.MailAuthEvent;
import com.smart.user.controller.dto.UserDto;
import com.smart.user.dao.UserDao;
import com.smart.user.domain.Status;
import com.smart.user.domain.User;
import com.smart.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import java.util.UUID;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

  private final UserDao userDao;

  private final ApplicationEventPublisher eventPublisher;

  private final HttpSession session;

  public UserServiceImpl(UserDao userDao,
      ApplicationEventPublisher eventPublisher, HttpSession session) {
    this.userDao = userDao;
    this.eventPublisher = eventPublisher;
    this.session = session;
  }

  @Override
  @Transactional
  public void join(UserDto.JoinRequest request) {
    if (userDao.checkUserMail(request.getEmail())) {
      throw new DuplicatedUserEmailException();
    }
    if (userDao.checkUserNickname(request.getNickname())) {
      throw new DuplicatedUserNicknameException();
    }
    userDao.joinUser(request.toEntity());

    String code = makeAuthCode();
    saveAuthCode(request.getEmail(), code);
    eventPublisher.publishEvent(new MailAuthEvent(request.getEmail(), code));
  }

  private String makeAuthCode() {
    return UUID.randomUUID().toString();
  }

  private void saveAuthCode(String email, String authCode) {
    session.setAttribute(email, authCode);
    session.setMaxInactiveInterval(60 * 60 * 24); // TTL 24시간
  }

  @Override
  public void verifyAuthCode(String email, String authCode) {
    if(authCode.equals((String) session.getAttribute(email))){
      session.removeAttribute(email);
      userDao.updateUserStatus(email, Status.NORMAL);
    }
    else throw new IllegalAuthCodeException();
  }

  @Override
  public UserDto.Response getUserByEmail(String email) {
    User user = userDao.getUserByEmail(email);
    if (user == null) {
      throw new NotFoundUserException();
    }
    return UserDto.Response.toResponse(user);
  }
}
