package ru.senla.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@ComponentScan("ru.senla")
public class SecurityConfig {
}
