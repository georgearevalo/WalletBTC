package wallet.service;

import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import wallet.model.WalletResponse;

@Service
@Slf4j
@RequiredArgsConstructor
public class WalletBTCService {

  //private final SaleProjectionCustomRepository saleProjectionCustomRepository;

  /**
   * @param xWalletBTC address wallet
   * @return found wallet
   */
  public WalletResponse getResponseWalletsEmpty(String xWalletBTC) {
    WalletResponse response = new WalletResponse();
    response.setWallet(Collections.emptyList());
    response.setNumberOfWallets(1);
    return response;
  }
}
