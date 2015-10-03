package com.softserve.edu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
@PropertySource("classpath:/properties/mail.properties")
@ComponentScan("com.softserve.edu.controller.service")
public class FileStorageConfig {

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver=new CommonsMultipartResolver();
        resolver.setDefaultEncoding("utf-8");
        resolver.setMaxUploadSize(5000000);
        return resolver;
    }
    
    @Bean
    public FormattingConversionService conversionService(){
        return new FormattingConversionService();
    }
    
}
