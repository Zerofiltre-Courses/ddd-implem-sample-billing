package tech.zerofiltre.freeland;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class FreelandBillingApplication {

  public static void main(String[] args) {
    SpringApplication.run(FreelandBillingApplication.class, args);
  }

}
