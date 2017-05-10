package com.springuni.commons.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.ControllerAdviceBean;
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
  public ControllerAdviceBean controllerAdviceBean() {
    Object controllerAdvice = createControllerAdvice();
    return new ControllerAdviceBean(controllerAdvice);
  }

  protected Object createControllerAdvice() {
    return new RestErrorHandler();
  }

  protected Object createDefaultHandler() {
    return new DefaultController();
  }

  @Override
  protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
    RequestMappingHandlerMapping handlerMapping = super.createRequestMappingHandlerMapping();
    Object defaultHandler = createDefaultHandler();
    handlerMapping.setDefaultHandler(defaultHandler);
    return handlerMapping;
  }

}
