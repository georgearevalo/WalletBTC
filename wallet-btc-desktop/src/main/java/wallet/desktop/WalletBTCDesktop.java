package wallet.desktop;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import okhttp3.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class WalletBTCDesktop extends Application {

    private static final String API_URL = "http://localhost:8080/wallet/send";
    private final OkHttpClient httpClient = new OkHttpClient();
    private final Gson gson = new Gson();

    private final Label statusLabel = new Label();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Bitcoin Wallet");

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);

        TextField addressField = new TextField();
        addressField.setPromptText("Recipient Address");

        TextField amountField = new TextField();
        amountField.setPromptText("Amount (BTC)");

        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> {
            String address = addressField.getText();
            String amountStr = amountField.getText();
            if (address.isEmpty() || amountStr.isEmpty()) {
                statusLabel.setText("Address and amount cannot be empty.");
                return;
            }
            try {
                BigDecimal amountBTC = new BigDecimal(amountStr);
                BigInteger amountSatoshis = amountBTC.multiply(new BigDecimal("100000000")).toBigInteger();

                sendBitcoin(address, amountSatoshis);
            } catch (NumberFormatException ex) {
                statusLabel.setText("Invalid amount format.");
            }
        });

        vbox.getChildren().addAll(
                new Label("Recipient Address:"),
                addressField,
                new Label("Amount (BTC):"),
                amountField,
                sendButton,
                statusLabel
        );

        Scene scene = new Scene(vbox, 400, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void sendBitcoin(String toAddress, BigInteger amount) {
        SendRequest requestPayload = new SendRequest(toAddress, amount);
        String jsonPayload = gson.toJson(requestPayload);

        RequestBody body = RequestBody.create(jsonPayload, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .build();

        statusLabel.setText("Sending...");

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                javafx.application.Platform.runLater(() -> statusLabel.setText("Failed to send: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    javafx.application.Platform.runLater(() -> statusLabel.setText("Transaction successful!"));
                } else {
                    String responseBody = response.body() != null ? response.body().string() : "Unknown error";
                    javafx.application.Platform.runLater(() -> statusLabel.setText("Error: " + response.code() + " " + responseBody));
                }
                response.close();
            }
        });
    }
}
