package wallet.desktop;

import java.math.BigInteger;

public class SendRequest {
    private final String toAddress;
    private final BigInteger amount;

    public SendRequest(String toAddress, BigInteger amount) {
        this.toAddress = toAddress;
        this.amount = amount;
    }

    public String getToAddress() {
        return toAddress;
    }

    public BigInteger getAmount() {
        return amount;
    }
}
