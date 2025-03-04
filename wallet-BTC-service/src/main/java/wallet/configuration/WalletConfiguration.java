package wallet.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Context;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.wallet.Wallet.BalanceType;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.script.Script.ScriptType;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.bitcoinj.core.InsufficientMoneyException;

import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.net.discovery.DnsDiscovery;

import org.bitcoinj.core.Coin;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.SendRequest;


import org.bitcoinj.store.BlockStore;
import org.bitcoinj.store.MemoryBlockStore;
import org.bitcoinj.core.BlockChain;
import org.bitcoinj.script.Script;

import org.bitcoinj.store.SPVBlockStore;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;

import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Transaction;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class WalletConfiguration {

  //private final WalletConfigurationProperties walletConfigurationProperties;
    
  @Bean
  public void getWalletConfiguration() {
    //log.info("walletSend {}", walletConfigurationProperties.getWalletSend());
    //log.info("walletReceive {}", walletConfigurationProperties.getWalletReceive());
    sendMoneyConnectPeerGroup4();
  }

  public void getTransactions() {
    try {
      // Configuración de la red principal
      NetworkParameters params = MainNetParams.get();
      Context context = new Context(params);

      // Cargar la wallet desde el archivo
      Wallet wallet = Wallet.loadFromFile(new File("simplewallet.wallet"));

      // Obtener el listado de transacciones
      List<Transaction> transactions = wallet.getTransactionsByTime();
        
      if (transactions.isEmpty()) {
        System.out.println("No transactions.");
      }

      // Imprimir la lista de transacciones
      for (Transaction tx : transactions) {
        System.out.println("Transacción: " + tx.getHashAsString());
        System.out.println("Valor: " + tx.getValue(wallet));
        System.out.println("Fecha: " + tx.getUpdateTime());
        System.out.println("--------------------------------------------------");
      }

    } catch (UnreadableWalletException e) {
      System.out.println("Ocurrió un error al getTransactions > " + e.getMessage());
    }
  }

  public void exportSeedPhrase() {
    try {
      // Configuración de la red principal
      NetworkParameters params = MainNetParams.get();
      Context context = new Context(params);

      // Cargar la wallet desde el archivo
      Wallet wallet = Wallet.loadFromFile(new File("simplewallet.wallet"));

      // Obtener la frase de recuperación (seed phrase)
      List<String> seedPhrase = wallet.getKeyChainSeed().getMnemonicCode();

      // Imprimir la frase de recuperación
      if (seedPhrase != null) {
        System.out.println("Frase de recuperación (seed phrase):");
        for (String word : seedPhrase) {
          System.out.print(word + " ");
        }
        System.out.println();
      } else {
        System.out.println("No se encontró una frase de recuperación.");
      }

    } catch (UnreadableWalletException e) {
      System.out.println("Ocurrió un error al exportSeedPhrase > " + e.getMessage());
    }
  }

  public void getKeys() {
    try {
      // Configuración de la red principal
      NetworkParameters params = MainNetParams.get();

      // Cargar la wallet desde el archivo
      Wallet wallet = Wallet.loadFromFile(new File("simplewallet.wallet"));

      // Obtener la dirección de recepción actual
      ECKey key = wallet.currentReceiveKey();

      // Exportar la clave privada en formato WIF (Wallet Import Format)
      DumpedPrivateKey privateKey = key.getPrivateKeyEncoded(params);
      System.out.println("Clave privada (WIF): " + privateKey.toString());

      // Si necesitas la clave privada en formato HEX
      System.out.println("Clave privada (HEX): " + key.getPrivateKeyAsHex());

    } catch (UnreadableWalletException e) {
      System.out.println("Ocurrió un error al getKeys -> " + e.getMessage());
    }
  }

  public void sendMoneyConnectPeerGroup4() {
    try {
      NetworkParameters params = MainNetParams.get(); // O TestNet3Params.get()
      File walletFile = new File("simplewallet.wallet");
      Wallet wallet;

      if (walletFile.exists()) {
        wallet = Wallet.loadFromFile(walletFile);
        System.out.println("Get wallet...");
        System.out.println("Address that sends funds: " + wallet.currentReceiveAddress().toString());
      } else {
        SecureRandom random = new SecureRandom();
        DeterministicSeed seed = new DeterministicSeed(random, 128, "");
        wallet = Wallet.fromSeed(params, seed, Script.ScriptType.P2PKH);
        wallet.saveToFile(walletFile);
        System.out.println("New wallet created");
      }

      BlockStore blockStore = new SPVBlockStore(params, new File("spvblockstore.spvchain"));
      BlockChain blockChain = new BlockChain(params, wallet, blockStore);

      PeerGroup peerGroup = new PeerGroup(params, blockChain);
      peerGroup.addPeerDiscovery(new DnsDiscovery(params));
      peerGroup.start();
      peerGroup.downloadBlockChain();

      Address toAddress = Address.fromString(params, "bc1qnmuq7vy4edtm6jlv5u8gssrh87ast8lksc25ng");
      Coin value = Coin.valueOf(1000);

      Wallet.SendResult result = wallet.sendCoins(peerGroup, toAddress, value);

      System.out.println("Transaction sent: " + result.tx.getTxId());

      peerGroup.stop();
    } catch (BlockStoreException | UnreadableWalletException | IOException | InsufficientMoneyException e) {
      System.out.println("Error sending BTC -> " + e.getMessage());
    }
  }
 
  public void sendMoneyConnectPeerGroup3() {
    try {
      // Configuración de la red principal o de pruebas
      NetworkParameters params = MainNetParams.get(); // Para pruebas, usa TestNet3Params.get();
      Context context = new Context(params);

      // Crear o cargar la wallet
      Wallet wallet;
      File walletFile = new File("simplewallet.wallet");
      if (walletFile.exists()) {
        // Carga la wallet existente
        wallet = Wallet.loadFromFile(walletFile);
        System.out.println("Get wallet...");
        System.out.println("Adress that send funds: " + wallet.currentReceiveAddress().toString());
      } else {
        // Crea una nueva wallet
        wallet = Wallet.createDeterministic(params, Script.ScriptType.P2PKH);
        wallet.saveToFile(walletFile);
        System.out.println("Creo nueva wallet");
      }

      // Configuración del BlockStore y BlockChain usando SPV
      BlockStore blockStore = new SPVBlockStore(params, new File("spvblockstore.spvchain"));
      BlockChain blockChain = new BlockChain(context, wallet, blockStore);

      // Configuración del PeerGroup para conectarse a la red
      PeerGroup peerGroup = new PeerGroup(context, blockChain);
      peerGroup.addPeerDiscovery(new DnsDiscovery(params));
      peerGroup.start();
      // Sincroniza la blockchain
      peerGroup.downloadBlockChain();

      // Generación de una dirección para recibir fondos
      Address receiveAddress = wallet.currentReceiveAddress();
      System.out.println("Dirección para recibir fondos: " + receiveAddress.toString());

      // Enviar Bitcoin
      Address toAddress = Address.fromString(params, "bc1qnmuq7vy4edtm6jlv5u8gssrh87ast8lksc25ng");
      Coin value = Coin.valueOf(1000); // Cantidad en satoshis (0.00001000 BTC)
      Wallet.SendResult result = wallet.sendCoins(peerGroup, toAddress, value);

      System.out.println("Transacción enviada: " + result.tx.getTxId());

      // Detener el PeerGroup al finalizar
      peerGroup.stop();
    } catch (BlockStoreException | UnreadableWalletException | IOException | InsufficientMoneyException e) {
      System.out.println("Ocurrió un error al enviar BTC -> " + e.getMessage());
    }
  }

  protected void sendMoneyConnectPeerGroup2() {
    try {
      // Configuración de la red principal o de pruebas
      NetworkParameters params = MainNetParams.get(); // Para pruebas, usa TestNet3Params.get();
      Context context = new Context(params);

      // Crear o cargar la wallet
      Wallet wallet;
      File walletFile = new File("simplewallet.wallet");
      if (walletFile.exists()) {
        // Carga la wallet existente
        wallet = Wallet.loadFromFile(walletFile);
        System.out.println("Cargo wallet");
      } else {
        // Crea una nueva wallet
        wallet = Wallet.createDeterministic(params, Script.ScriptType.P2PKH);
        wallet.saveToFile(walletFile);
        System.out.println("Creo nueva wallet");
      }

      // Configuración del BlockStore y BlockChain usando SPV
      BlockStore blockStore = new SPVBlockStore(params, new File("spvblockstore.spvchain"));
      BlockChain blockChain = new BlockChain(context, wallet, blockStore);
      // Configuración del PeerGroup para conectarse a la red
      PeerGroup peerGroup = new PeerGroup(context, blockChain);
      peerGroup.addPeerDiscovery(new DnsDiscovery(params));
      peerGroup.start();
      // Sincroniza la blockcain (puede tomar menos tiempo con SPV)
      peerGroup.downloadBlockChain();

      // Generación de una dirección para recibir fondos
      Address receiveAddress = wallet.currentReceiveAddress();
      System.out.println("Dirección para recibir fondos: " + receiveAddress.toString());

      // Enviar Bitcoin
      Address toAddress = Address.fromString(params, "bc1qnmuq7vy4edtm6jlv5u8gssrh87ast8lksc25ng");
      Coin value = Coin.valueOf(1000); // Cantidad en satoshis (0.00001000 BTC)
      Wallet.SendResult result = wallet.sendCoins(peerGroup, toAddress, value);

      System.out.println("Transacción enviada: " + result.tx.getTxId());

      // Detener el PeerGroup al finalizar
      peerGroup.stop();
    } catch (BlockStoreException | UnreadableWalletException | IOException | InsufficientMoneyException e) {
      System.out.println("Ocurrio un error al enviar BTC -> " + e.getMessage());
    }
  }

  public void sendMoneyConnectPeerGroup() {
    try {
      // **Configuración de la red principal o de pruebas**
      NetworkParameters params = MainNetParams.get(); // Para pruebas, usa TestNet3Params.get();
      Context context = new Context(params);

      // **Creación o carga de la wallet**
      Wallet wallet;
      File walletFile = new File("simplewallet.wallet");
      if (walletFile.exists()) {
        // Carga la wallet existente
        wallet = Wallet.loadFromFile(walletFile);
        System.out.println("cargo wallet");
      } else {
        // Crea una nueva wallet
        wallet = Wallet.createDeterministic(params, Script.ScriptType.P2PKH);
        wallet.saveToFile(walletFile);
        System.out.println("creo nueva wallet");
      }

      // **Configuración del BlockStore y BlockChain**
      BlockStore blockStore = new MemoryBlockStore(params);
      BlockChain blockChain = new BlockChain(context, wallet, blockStore);

      // **Configuración del PeerGroup para conectarse a la red**
      PeerGroup peerGroup = new PeerGroup(context, blockChain);
      peerGroup.addPeerDiscovery(new DnsDiscovery(params));
      peerGroup.start();
      // Sincroniza la blockchain (puede tomar tiempo)
      peerGroup.downloadBlockChain();

      // **Generación de una dirección para recibir fondos**
      Address receiveAddress = wallet.currentReceiveAddress();
      System.out.println("Dirección para recibir fondos: " + receiveAddress.toString());

      // **Enviar Bitcoin**
      // Reemplaza "direccion_destinatario" con una dirección válida
      Address toAddress = Address.fromString(params, "bc1qnmuq7vy4edtm6jlv5u8gssrh87ast8lksc25ng");
      Coin value = Coin.valueOf(1000); // Cantidad en satoshis (0.00001000 BTC)
      Wallet.SendResult result = wallet.sendCoins(peerGroup, toAddress, value);

      System.out.println("Transacción enviada: " + result.tx.getTxId());

      // **Detener el PeerGroup al finalizar**
      peerGroup.stop();
    } catch (BlockStoreException | UnreadableWalletException | IOException | InsufficientMoneyException e) {
      System.out.println("Ocurrio un error al enviar BTC -> " + e.getMessage());
    }
  }

  protected void sendMoney() {
    try {
      NetworkParameters params = MainNetParams.get();
      Wallet wallet = Wallet.loadFromFile(new File("simplewallet.wallet"));
        
      // Define la dirección del destinatario
      Address toAddress = Address.fromString(params, "bc1qnmuq7vy4edtm6jlv5u8gssrh87ast8lksc25ng");
      Coin value = Coin.valueOf(1000); // Cantidad en satoshis (10000 satoshis = 0.001 BTC)
        
      /*
        Sats                Bitcoin                 Nombre
        100 Satoshi  0.00000100 BTC          1 Bit
        1,000 Satoshi  0.00001000 BTC
        10,000 Satoshi  0.00010000 BTC
        100,000 Satoshi  0.00100000 BTC          1mBTC
      */
        
      // Crea una solicitud de envío
      SendRequest request = SendRequest.to(toAddress, value);
       
      // Firma la transacción con la wallet
      wallet.completeTx(request);
        
      // Envía la transacción a la red (este paso puede requerir un nodo conectado)
      wallet.commitTx(request.tx);
      System.out.println("Transacción enviada: " + request.tx.getTxId());
    } catch (UnreadableWalletException | InsufficientMoneyException e) {
      System.out.println("Ocurrio un error a enviar BTC -> " + e.getMessage());
    }
  }

  protected void checkBalance() {
    try {
      System.out.println("checkBalance1");
      NetworkParameters params = MainNetParams.get();
      Context context = new Context(params);
      System.out.println("checkBalance2");
      // Carga la wallet desde el archivo
      Wallet wallet = Wallet.loadFromFile(new File("simplewallet.wallet"));
      System.out.println("checkBalance3");
      // Configuración del BlockStore y BlockChain
      BlockStore blockStore = new MemoryBlockStore(params);
      BlockChain blockChain = new BlockChain(context, wallet, blockStore);
      System.out.println("checkBalance4");
      // Configuración del PeerGroup para conectarse a la red
      PeerGroup peerGroup = new PeerGroup(context, blockChain);
      System.out.println("checkBalance5");
      peerGroup.addPeerDiscovery(new DnsDiscovery(params));
      System.out.println("checkBalance6");
      peerGroup.start();
      System.out.println("checkBalance7");
      peerGroup.downloadBlockChain();
      System.out.println("checkBalance8");

      // Obtiene el saldo
      System.out.println("Saldo total: " + wallet.getBalance().toFriendlyString());
      System.out.println("Saldo disponible: " + wallet.getBalance(BalanceType.AVAILABLE).toFriendlyString());

      // Detener el PeerGroup al finalizar
      peerGroup.stop();

    } catch (BlockStoreException | UnreadableWalletException e) {
      System.out.println("Ocurrio un error a checkBalance -> " + e.getMessage());
    }
  }

  public String createWallet() {
    try {
      // Configura la red principal de Bitcoin
      NetworkParameters params = MainNetParams.get();
      Context context = new Context(params);
      Wallet wallet = Wallet.createDeterministic(context, ScriptType.P2PKH);
        
      // Guarda la wallet en un archivo
      wallet.saveToFile(new File("simplewallet.wallet"));
        
      // Carga la wallet desde el archivo
      Wallet loadedWallet = Wallet.loadFromFile(new File("simplewallet.wallet"));
        
      // Genera una nueva dirección para recibir fondos
      Address receiveAddress = loadedWallet.currentReceiveAddress();
      System.out.println("Dirección para recibir fondos: " + receiveAddress.toString());
      return receiveAddress.toString();
    } catch (UnreadableWalletException | IOException e) {
      System.out.println("Ocurrio un error a createWallet -> " + e.getMessage());
    }
    return null;
  }  
}
