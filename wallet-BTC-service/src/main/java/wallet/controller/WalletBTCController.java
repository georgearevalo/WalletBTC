package wallet.controller;

import wallet.model.*;
import wallet.service.WalletBTCService;
//import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@Slf4j
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class WalletBTCController implements WalletBTCServicesApi {

  private final WalletBTCService walletBTCService;

  @Override
  public ResponseEntity<WalletResponse> getWalletBTC(String xWalletBTC) {
    log.info("getWalletBTC method called");
    log.debug("walletBTC: {}", xWalletBTC);

    return new ResponseEntity<>(walletBTCService.getResponseWalletsEmpty(xWalletBTC), HttpStatus.OK);

  }
}
