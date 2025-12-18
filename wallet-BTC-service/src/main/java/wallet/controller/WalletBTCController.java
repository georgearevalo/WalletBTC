package wallet.controller;

import wallet.model.*;
import wallet.service.WalletBTCService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import java.util.Optional;

@RestController
@Slf4j
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class WalletBTCController implements WalletBTCServicesApi, WalletApi {

  private final WalletBTCService walletBTCService;

  @Override
  public Optional<NativeWebRequest> getRequest() {
    return WalletBTCServicesApi.super.getRequest();
  }

  @Override
  public ResponseEntity<WalletResponse> getWalletBTC(String xWalletBTC) {
    log.info("getWalletBTC method called");
    log.debug("walletBTC: {}", xWalletBTC);

    return new ResponseEntity<>(walletBTCService.getWalletInfo(xWalletBTC), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> sendBitcoin(SendRequest sendRequest) {
    try {
      walletBTCService.sendBitcoin(sendRequest);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      log.error("Error in sendBitcoin controller", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
