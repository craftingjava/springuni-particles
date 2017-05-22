package com.springuni.auth.boot;

import com.springuni.auth.domain.model.user.UserJpaRepositoryConfiguration;
import com.springuni.auth.domain.service.UserServiceConfiguration;
import com.springuni.auth.rest.AuthRestConfiguration;
import com.springuni.auth.security.AuthSecurityConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by lcsontos on 5/18/17.
 */
@SpringBootApplication
@Configuration
@Import({
    UserJpaRepositoryConfiguration.class,
    UserServiceConfiguration.class,
    AuthRestConfiguration.class,
    AuthSecurityConfiguration.class
})
public class Application {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Application.class, args);
  }

}
