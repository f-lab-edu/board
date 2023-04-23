package com.smart.user.service.implement;

import com.smart.global.error.DuplicatedUserEmailException;
import com.smart.global.error.DuplicatedUserNicknameException;
import com.smart.global.error.IllegalAuthCodeException;
import com.smart.global.error.NotFoundUserException;
import com.smart.mail.event.MailAuthEvent;
import com.smart.user.controller.dto.UserDto;
import com.smart.user.controller.dto.UserDto.UserInfo;
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
  public User join(UserDto.JoinRequest request) {
    if (userDao.checkUserMail(request.getEmail())) {
      throw new DuplicatedUserEmailException();
    }
    if (userDao.checkUserNickname(request.getNickname())) {
      throw new DuplicatedUserNicknameException();
    }

    String code = makeAuthCode();
    System.out.println("code:"+code);
    saveAuthCode(request.getEmail(), code);
    eventPublisher.publishEvent(new MailAuthEvent(request.getEmail(), code));

    User user = request.toEntity();
    userDao.joinUser(user);
    return user;
  }

  private String makeAuthCode() {
    return UUID.randomUUID().toString();
  }

  private void saveAuthCode(String email, String authCode) {
    session.setAttribute(email, authCode);
    session.setMaxInactiveInterval(60 * 60 * 24); // TTL 24시간
  }

  @Override
  public boolean verifyAuthCode(String email, String authCode) {
    String storedAuthCode = (String) session.getAttribute(email);
    if (storedAuthCode == null || !storedAuthCode.equals(authCode)) {
      throw new IllegalAuthCodeException();
    }
    session.removeAttribute(email);
    userDao.updateUserStatus(email, Status.NORMAL);
    return true;
  }

  @Override
  public UserInfo getUserByEmail(String email) {
    User user = userDao.getUserByEmail(email)
        .orElseThrow(NotFoundUserException::new);
    return UserDto.UserInfo.from(user);
  }

  @Override
  public boolean updateUserInfo(User user) {
    boolean isNicknameDuplicated = userDao.checkUserNickname(user.getNickname());
    if (isNicknameDuplicated) {
      throw new DuplicatedUserNicknameException();
    }
    return userDao.updateUserInfoByEmail(user);
  }


  @Override
  public boolean isDuplicateNickname(String nickname) {
    return userDao.checkUserNickname(nickname);
  }
}