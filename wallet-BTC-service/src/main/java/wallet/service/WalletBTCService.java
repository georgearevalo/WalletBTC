package wallet.service;

import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.bitcoinj.base.Address;
import org.bitcoinj.base.Coin;
import org.bitcoinj.core.Context;
import org.bitcoinj.core.InsufficientMoneyException;

import wallet.configuration.WalletConfiguration;
import wallet.model.WalletResponse;

@Service
@Slf4j
@RequiredArgsConstructor
public class WalletBTCService {

  private final WalletConfiguration walletConfiguration;

  public WalletResponse getWalletInfo(String xWalletBTC) {
    new Context(walletConfiguration.getParams());
    Coin balance = walletConfiguration.getBalance();
    Address address = walletConfiguration.getCurrentReceiveAddress();
    log.info("Wallet address: {}", address.toString());
    log.info("Wallet balance: {}", balance.toFriendlyString());

    WalletResponse response = new WalletResponse();
    response.setWallet(Collections.emptyList());
    response.setNumberOfWallets(1);
    response.setAddress(address.toString());
    response.setBalance(balance.toFriendlyString());
    return response;
  }

  public void sendBitcoin(wallet.model.SendRequest sendRequest) throws InsufficientMoneyException {
    try {
      new Context(walletConfiguration.getParams());
      Address toAddress = Address.fromString(walletConfiguration.getParams(), sendRequest.getToAddress());
      Coin value = Coin.valueOf(sendRequest.getAmount());

      // Create a SendRequest to customize the transaction
      org.bitcoinj.wallet.SendRequest req = org.bitcoinj.wallet.SendRequest.to(toAddress, value);
      
      // Set a custom fee rate (e.g., 1000 satoshis per KB)
      req.feePerKb = Coin.valueOf(1000);

      log.info("Sending from wallet: {}", walletConfiguration.getCurrentReceiveAddress());
      log.info("Attempting to send {} to {} with a fee rate of {}", value.toFriendlyString(), toAddress, req.feePerKb.toFriendlyString());
      walletConfiguration.sendCoins(req);
      log.info("Successfully initiated sending of funds.");

    } catch (InsufficientMoneyException e) {
      log.error("Error sending BTC: Not enough money.", e);
      throw e; 
    } catch (Exception e) {
      log.error("An unexpected error occurred during sendBitcoin.", e);
    }
  }
}