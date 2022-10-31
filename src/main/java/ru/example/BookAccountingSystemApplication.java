package ru.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
@ComponentScan("ru.example")
public class BookAccountingSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookAccountingSystemApplication.class, args);
    }
}
