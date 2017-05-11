package com.springuni.commons.rest;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Created by lcsontos on 5/10/17.
 */
@EnableWebMvc
@Configuration
public class RestConfiguration extends WebMvcConfigurationSupport {

  @Bean
  public Object controllerAdvice() {
    return createControllerAdvice();
  }

  @Bean
  public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();
    customizeModelMapper(modelMapper);
    return modelMapper;
  }

  @Override
  @Bean
  @DependsOn("controllerAdvice")
  public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
    return super.requestMappingHandlerAdapter();
  }

  protected Object createControllerAdvice() {
    return new RestErrorHandler();
  }

  protected Object createDefaultHandler() {
    return new DefaultController();
  }

  protected void customizeModelMapper(ModelMapper modelMapper) {
  }

  @Override
  protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
    RequestMappingHandlerMapping handlerMapping = super.createRequestMappingHandlerMapping();
    Object defaultHandler = createDefaultHandler();
    handlerMapping.setDefaultHandler(defaultHandler);
    return handlerMapping;
  }

}
