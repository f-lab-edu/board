package com.smart.user.service;

import com.smart.global.error.NotFoundUserException;
import com.smart.user.dao.UserDao;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class UserSecurityServiceTest {
  private UserSecurityService userSecurityService;
  @Mock
  private UserService userService;

  @BeforeEach
  public void setUp() {
    userSecurityService = new UserSecurityService(userService, null);
  }

  @Test
  public void testLoadUserByUsernameWithNotFoundUserException() {
    String email = "invalidEmail@example.com";
    when(userService.getUserByEmail(email)).thenReturn(Optional.empty());

    Assertions.assertThrows(
        NotFoundUserException.class,
        () -> userSecurityService.loadUserByUsername(email)
    );
  }
}

