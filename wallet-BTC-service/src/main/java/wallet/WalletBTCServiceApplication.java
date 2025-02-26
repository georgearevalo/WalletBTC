package wallet;

import wallet.configuration.AwsAthenaConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AwsAthenaConfigurationProperties.class)
public class WalletBTCServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(WalletBTCServiceApplication.class, args);
  }
}
