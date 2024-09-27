package shop.biday;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;

@SpringBootApplication
@EnableReactiveMongoAuditing
public class BidayApplication {

    public static void main(String[] args) {
        SpringApplication.run(BidayApplication.class, args);
    }

}

