package wallet.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.base.Address;
import org.bitcoinj.base.Coin;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.Context;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.crypto.ECKey;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.wallet.Wallet;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.File;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Getter
public class WalletConfiguration {

  private WalletAppKit kit;
  private NetworkParameters params;

  @PostConstruct
  public void init() {
    params = MainNetParams.get();
    new Context(params);
    kit = new WalletAppKit(params, new File("."), "simplewallet") {
      @Override
      protected void onSetupCompleted() {
        peerGroup().setConnectTimeoutMillis(2000);
        peerGroup().setMaxConnections(16);
      }
    };

    // Start the kit and wait for it to be ready
    kit.startAsync();
    kit.awaitRunning();

    log.info("BitcoinJ WalletAppKit initialized.");
    log.info("Wallet address: {}", kit.wallet().currentReceiveAddress().toString());
  }

  @PreDestroy
  public void shutdown() {
    if (kit != null && kit.isRunning()) {
      log.info("Stopping WalletAppKit...");
      kit.stopAsync();
      kit.awaitTerminated();
      log.info("WalletAppKit stopped.");
    }
  }

  public List<Transaction> getTransactions() {
    return kit.wallet().getTransactionsByTime();
  }

  public List<String> getSeedPhrase() {
    if (kit.wallet().getKeyChainSeed() != null) {
      return kit.wallet().getKeyChainSeed().getMnemonicCode();
    }
    return null;
  }

  public String getPrivateKeyAsWIF() {
    ECKey key = kit.wallet().currentReceiveKey();
    return key.getPrivateKeyEncoded(params).toString();
  }

  public String getPrivateKeyAsHex() {
    ECKey key = kit.wallet().currentReceiveKey();
    return key.getPrivateKeyAsHex();
  }

  public Wallet.SendResult sendCoins(org.bitcoinj.wallet.SendRequest req) throws InsufficientMoneyException {
    log.info("Attempting to send coins with SendRequest");
    Wallet.SendResult result = kit.wallet().sendCoins(req);
    log.info("Transaction broadcasted: {}", result.tx.getTxId());
    return result;
  }

  public Coin getBalance() {
    return kit.wallet().getBalance();
  }

  public Coin getAvailableBalance() {
    return kit.wallet().getBalance(Wallet.BalanceType.AVAILABLE);
  }

  public Address getCurrentReceiveAddress() {
    return kit.wallet().currentReceiveAddress();
  }
}
