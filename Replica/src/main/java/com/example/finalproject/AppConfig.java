package com.example.finalproject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
/**
 * This annotation is used to mark a class as the configuration class.
 * The @Configuration is a type of @Component annotation.
 * The @Configuration annotation indicates that the class can be used by the Spring IoC container as a source of bean definitions.
 * The @Configuration annotation is used on top of the class definition.
 */
public class AppConfig {
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.failOnUnknownProperties(false);
        return new MappingJackson2HttpMessageConverter(builder.build());
    }
}

