package com.softserve.edu.config;

import com.softserve.edu.service.utils.ProcessTimeChecker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * Spring MVC config for the servlet context in the application.
 * <p/>
 * The beans of this context are only visible inside the servlet context.
 */
@Configuration
@EnableWebMvc
@EnableAsync
@EnableScheduling
@ComponentScan("com.softserve.edu.controller")
@PropertySource("classpath:/properties/mail.properties")
public class ServletContextConfig extends WebMvcConfigurerAdapter {
    
    @Value("${photo.storage}") 
    private String storageLocation;
    
    @Value("${photo.path}")
    private String photoPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**", photoPath).addResourceLocations("/resources/", storageLocation);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter());
        converters.add(new StringHttpMessageConverter());
    }
    
    @Bean
    public FormattingConversionService conversionService(){
        return new FormattingConversionService();
    }
    
    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver getMultipartResolver() {
        CommonsMultipartResolver resolver=new CommonsMultipartResolver();
        resolver.setDefaultEncoding("utf-8");
        resolver.setMaxUploadSize(5000000);
        return resolver;
    }
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigIn() {
        return new PropertySourcesPlaceholderConfigurer();
    }
    
    @Bean
    public  ProcessTimeChecker  getChecker() {
        return new ProcessTimeChecker();
    }

}