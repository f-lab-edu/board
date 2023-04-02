package com.smart.config;

import static org.springframework.security.config.Customizer.withDefaults;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {

  @Bean
  public BCryptPasswordEncoder encodePwd() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf().disable().cors().disable()
        .authorizeHttpRequests(request -> request
            .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
            .requestMatchers("/","/login","/api/v1/user/**","/join").permitAll()
            .anyRequest().authenticated() //모든 요청 인청요청
        )
        .formLogin(login -> login
            .loginPage("/login")
            .usernameParameter("email")
            .passwordParameter("password")
            .defaultSuccessUrl("/mypage", false)
            .failureUrl("/login?error")// 로그인 실패 시 로그인 페이지에 머무르도록 변경
            .permitAll()
        );

    http.logout()
        .logoutSuccessUrl("/login")
        .invalidateHttpSession(true);   // 세션 날리기

    return http.build();
  }

}
