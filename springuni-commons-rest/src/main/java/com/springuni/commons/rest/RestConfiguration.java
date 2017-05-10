package com.springuni.commons.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Created by lcsontos on 5/10/17.
 */
@EnableWebMvc
@Configuration
public class RestConfiguration extends WebMvcConfigurationSupport {

  @Bean
  public DefaultController defaultController() {
    return new DefaultController();
  }

  @Override
  protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
    RequestMappingHandlerMapping handlerMapping = super.createRequestMappingHandlerMapping();
    handlerMapping.setDefaultHandler("defaultController");
    return handlerMapping;
  }

}
