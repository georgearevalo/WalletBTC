package wallet.exceptions;

import org.springframework.http.HttpStatus;

import wallet.exceptions.catalog.WalletBTCExceptionCatalog;

public class WalletNotFoundException extends WalletBTCException {

  public WalletNotFoundException() {
    super(HttpStatus.NOT_FOUND,
      WalletBTCExceptionCatalog.WALLET_NOT_FOUND_ERROR_CODE,
      WalletBTCExceptionCatalog.WALLET_NOT_FOUND_ERROR_MESSAGE);
  }

}
