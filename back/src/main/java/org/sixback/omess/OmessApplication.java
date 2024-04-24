package org.sixback.omess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class OmessApplication {

    public static void main(String[] args) {
        SpringApplication.run(OmessApplication.class, args);
    }

}
