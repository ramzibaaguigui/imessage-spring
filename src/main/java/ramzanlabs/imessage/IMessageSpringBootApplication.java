package ramzanlabs.imessage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IMessageSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(IMessageSpringBootApplication.class, args);
    }
}
