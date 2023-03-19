package com.smart.user.service.Implement;

import com.smart.global.error.DuplicatedUserEmailException;
import com.smart.global.error.DuplicatedUserNicknameException;
import com.smart.global.error.NotFoundUserException;
import com.smart.mail.event.MailEvent;
import com.smart.user.controller.dto.UserDto;
import com.smart.user.dao.UserDao;
import com.smart.user.domain.User;
import com.smart.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import java.util.UUID;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserDao userDao;

  private final HttpSession httpSession;

  private final ApplicationEventPublisher eventPublisher;

  public UserServiceImpl(UserDao userDao, HttpSession httpSession,
      ApplicationEventPublisher eventPublisher) {
    this.userDao = userDao;
    this.httpSession = httpSession;
    this.eventPublisher = eventPublisher;
  }

  @Override
  public void join(UserDto.JoinRequest request) {
    if (userDao.checkUserMail(request.getEmail())) {
      throw new DuplicatedUserEmailException();
    }
    if (userDao.checkUserNickname(request.getNickname())) {
      throw new DuplicatedUserNicknameException();
    }

    String code = makeAuthCode();
    eventPublisher.publishEvent(new MailEvent(request.getEmail(), code));
    saveAuthCode(request.getEmail(), code);
    userDao.joinUser(request.toEntity());
  }

  @Override
  public String makeAuthCode() {
    return UUID.randomUUID().toString();
  }

  @Override
  public void saveAuthCode(String email, String code) {
    httpSession.setAttribute(email, code);
    httpSession.setMaxInactiveInterval(60 * 60 * 24); // TTL 24시간
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
