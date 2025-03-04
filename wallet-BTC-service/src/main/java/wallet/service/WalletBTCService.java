package wallet.service;

import java.util.Collections;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import wallet.configuration.WalletConfiguration;
import wallet.entity.Firsttb;
import wallet.model.WalletResponse;
import wallet.repository.FirsttbRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class WalletBTCService {

  private final FirsttbRepository firsttbRepository;
  private final WalletConfiguration walletConfiguration;

  /**
   * @param xWalletBTC address wallet
   * @return found wallet
   */
  public WalletResponse getResponseWalletsEmpty(String xWalletBTC) {
    Optional<Firsttb> firts = firsttbRepository.findFirsttbByCodigo(1);
    if (firts.isPresent()) {
      Firsttb data = firts.get();
      log.info("find {}-{}.", data.getCodigo(), data.getNombre());
    } else {
      log.error("No found data.");
    }

    //walletConfiguration.getWalletConfiguration();
    //walletConfiguration.createWallet();
    //walletConfiguration.getKeys();
    walletConfiguration.sendMoneyConnectPeerGroup4();

    WalletResponse response = new WalletResponse();
    response.setWallet(Collections.emptyList());
    response.setNumberOfWallets(1);
    return response;
  }
}
