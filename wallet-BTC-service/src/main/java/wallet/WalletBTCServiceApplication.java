package wallet;

import wallet.configuration.WalletConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(WalletConfigurationProperties.class)
public class WalletBTCServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(WalletBTCServiceApplication.class, args);
  }
}
