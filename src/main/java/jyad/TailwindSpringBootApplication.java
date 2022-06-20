package jyad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TailwindSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(TailwindSpringBootApplication.class, args);
    }
}
