package com.smart.user.service;

import com.smart.global.error.DuplicatedUserEmailException;
import com.smart.global.error.DuplicatedUserNicknameException;
import com.smart.global.error.IllegalAuthCodeException;
import com.smart.global.error.NotFoundUserException;
import com.smart.mail.event.MailAuthEvent;
import com.smart.user.controller.dto.UserInfoDto;
import com.smart.user.controller.dto.UserSaveDto;
import com.smart.user.controller.dto.UserUpdateDto;
import com.smart.user.domain.Status;
import com.smart.user.domain.User;
import com.smart.user.repository.AuthCodeRepository;
import com.smart.user.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

  private final UserRepository userRepository;

  private final ApplicationEventPublisher eventPublisher;

  private final AuthCodeRepository authCodeRepository;

  public UserService(UserRepository userRepository, ApplicationEventPublisher eventPublisher,
      AuthCodeRepository authCodeRepository) {
    this.userRepository = userRepository;
    this.eventPublisher = eventPublisher;
    this.authCodeRepository = authCodeRepository;
  }

  @Transactional
  public Long join(UserSaveDto saveDto) {
    if (userRepository.existsByEmail(saveDto.getEmail())) {
      throw new DuplicatedUserEmailException();
    }
    if (userRepository.existsByNickname(saveDto.getNickname())) {
      throw new DuplicatedUserNicknameException();
    }

    String authCode = authCodeRepository.getAuthCode(saveDto.getEmail());
    authCodeRepository.saveAuthCode(saveDto.getEmail(), authCode);
    eventPublisher.publishEvent(new MailAuthEvent(saveDto.getEmail(), authCode));

    return userRepository.save(saveDto.toEntity());
  }

  public void verifyAuthCode(String email, String authCode) {
    String storedAuthCode = authCodeRepository.getAuthCode(email);
    if (storedAuthCode == null || !storedAuthCode.equals(authCode)) {
      throw new IllegalAuthCodeException();
    }
    authCodeRepository.removeAuthCode(email);

    User user = userRepository.findByEmail(email)
        .orElseThrow(NotFoundUserException::new);

    user.updateUserStatus(Status.NORMAL);
    userRepository.save(user);
  }

  public UserInfoDto getUserByEmail(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(NotFoundUserException::new);
    return UserInfoDto.from(user);
  }

  public void updateUserInfo(UserUpdateDto userUpdateDto) {
    User user = userRepository.findByEmail(userUpdateDto.getEmail())
        .orElseThrow(NotFoundUserException::new);

    isDuplicateNickname(userUpdateDto.getNickname());

    user.updateName(userUpdateDto.getName());
    user.updateNickname(userUpdateDto.getNickname());
    user.updatePassword(userUpdateDto.getPassword());

    userRepository.save(userUpdateDto.toEntity(user));
  }

  public void isDuplicateNickname(String nickname) {
    if (userRepository.existsByNickname(nickname)) {
      throw new DuplicatedUserNicknameException();
    }
  }
}