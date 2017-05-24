package com.springuni.commons.rest;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_ABSENT;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Created by lcsontos on 5/10/17.
 */
@EnableWebMvc
@Configuration
public class RestConfigurationSupport extends WebMvcConfigurationSupport {

  @Bean
  public Object controllerAdvice() {
    return createControllerAdvice();
  }

  @Bean
  public ObjectMapper objectMapper() {
    // Registers com.fasterxml.jackson.datatype.jsr310.JavaTimeModule automatically
    ObjectMapper objectMapper = Jackson2ObjectMapperBuilder
        .json()
        .indentOutput(true)
        .featuresToDisable(WRITE_DATES_AS_TIMESTAMPS)
        .serializationInclusion(NON_ABSENT)
        .build();
    customizeObjectMapper(objectMapper);
    return objectMapper;
  }

  @Override
  @Bean
  @DependsOn("controllerAdvice")
  public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
    return super.requestMappingHandlerAdapter();
  }

  @Override
  protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    ObjectMapper objectMapper = objectMapper();
    converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
  }

  protected Object createControllerAdvice() {
    return new RestErrorHandler();
  }

  protected Object createDefaultHandler() {
    return new DefaultController();
  }

  protected void customizeObjectMapper(ObjectMapper objectMapper) {
  }

  @Override
  protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
    RequestMappingHandlerMapping handlerMapping = super.createRequestMappingHandlerMapping();
    Object defaultHandler = createDefaultHandler();
    handlerMapping.setDefaultHandler(defaultHandler);
    return handlerMapping;
  }

}
