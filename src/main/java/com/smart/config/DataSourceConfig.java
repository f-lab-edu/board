package com.smart.config;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {

  /**
   * HikariCP 명시
   * - SpringBoot2.0부터 HikariCP가 기본 Datasource이며 Java Config 설정 시 jdbc-url을 사용해야하지만 추후 자동설정으로 변경시엔 url을 사용해야한다.
   * 이에 대한 혼돈을 방지하기 위해 따로 명시함.
   */
  @Bean
  @ConfigurationProperties(prefix = "spring.datasource.hikari")
  public DataSource dataSource() {
    return DataSourceBuilder.create().type(HikariDataSource.class).build();
  }
}

