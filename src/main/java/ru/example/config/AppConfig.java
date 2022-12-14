package ru.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.example.util.EmailSender;

@Configuration
@ComponentScan("ru.example")
@PropertySource("classpath:application.properties")
@EnableWebMvc
public class AppConfig {
    @Value("${email.sender.username}")
    private String username;
    @Value("${email.sender.password}")
    private String password;

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurerBean() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public EmailSender emailSenderBean() {
        return new EmailSender(username, password);
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
}
