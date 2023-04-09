package com.smart.user.service;

import com.smart.global.error.NotFoundUserException;
import com.smart.user.dao.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class UserSecurityServiceTest {
  private UserSecurityService userSecurityService;

  @Mock
  private UserDao userDao;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    userSecurityService = new UserSecurityService(userDao, null);
  }

  @Test
  public void testLoadUserByUsernameWithNotFoundUserException() {
    // given
    String email = "invalidEmail@example.com";
    when(userDao.getUserByEmail(email)).thenReturn(null);

    // when, then
    assertThrows(NotFoundUserException.class, () -> userSecurityService.loadUserByUsername(email));
  }
}
