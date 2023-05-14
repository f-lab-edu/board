package com.smart.user.service;

import com.smart.global.error.DuplicatedUserEmailException;
import com.smart.global.error.DuplicatedUserNicknameException;
import com.smart.global.error.IllegalAuthCodeException;
import com.smart.mail.event.MailAuthEvent;
import com.smart.mail.service.MailService;
import com.smart.user.controller.dto.UserInfoDto;
import com.smart.user.controller.dto.UserSaveDto;
import com.smart.user.controller.dto.UserUpdateDto;
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

  private final MailService mailService;

  public UserService(UserRepository userRepository, ApplicationEventPublisher eventPublisher,
      AuthCodeRepository authCodeRepository, MailService mailService) {
    this.userRepository = userRepository;
    this.eventPublisher = eventPublisher;
    this.authCodeRepository = authCodeRepository;
    this.mailService = mailService;
  }

  @Transactional
  public Long join(UserSaveDto saveDto) {
    checkDuplicateEmail(saveDto.getEmail());
    checkDuplicateNickname(saveDto.getNickname());

    User user = saveDto.toEntity();
    String authCode = getAuthCode();
    userRepository.save(user);

    eventPublisher.publishEvent(new MailAuthEvent(user.getEmail(), authCode));
    return user.getUserId();
  }

  private void checkDuplicateEmail(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new DuplicatedUserEmailException();
    }
  }

  public void verifyAuthCode(String email, String authCode) {
    String storedAuthCode = authCodeRepository.getAuthCode(email);
    if (storedAuthCode == null || !storedAuthCode.equals(authCode)) {
      throw new IllegalAuthCodeException();
    }
    authCodeRepository.removeAuthCode(email);

    User verifiedUser = userRepository.findByEmail(email);

    verifiedUser.approveUserAuth();
    userRepository.save(verifiedUser);
  }

  public UserInfoDto getUserByEmail(String email) {
    User user = userRepository.findByEmail(email);
    return UserInfoDto.from(user);
  }

  public void updateUserInfo(UserUpdateDto userUpdateDto) {
    User user = userRepository.findByEmail(userUpdateDto.getEmail());

    if(user.isNicknameChanged(userUpdateDto.getNickname())){
      checkDuplicateNickname(userUpdateDto.getNickname());
    }

    userRepository.save(userUpdateDto.toEntity(user));
  }

  public void checkDuplicateNickname(String nickname) {
    if (userRepository.existsByNickname(nickname)) {
      throw new DuplicatedUserNicknameException();
    }
  }

  public void resetPassword(String userEmail) {
    User user = userRepository.findByEmail(userEmail);

    String temporaryPassword = getAuthCode();
    saveUserWithTemporaryPassword(user, temporaryPassword);

    saveAuthCode(user, temporaryPassword);
    sendResetPasswordEmail(user, temporaryPassword);
  }

  private void sendResetPasswordEmail(User user, String temporaryPassword) {
    mailService.sendPasswordResetEmail(user.getEmail(), temporaryPassword);
  }

  private void saveAuthCode(User user, String temporaryPassword) {
    authCodeRepository.saveAuthCode(user.getEmail(), temporaryPassword);
  }

  private void saveUserWithTemporaryPassword(User user, String temporaryPassword) {
    user.updateTemporaryPassword(temporaryPassword);
    userRepository.save(user);
  }

  private String getAuthCode() {
    return authCodeRepository.generateAuthCode();
  }
}