package com.limpet1.rest.ascendex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Base64;

public class AscendexConnectorTest {

    String path = "balance";

    public String senderMethod(String apiKey, String secret, int group) throws IOException, InterruptedException {

        long timesamp = System.currentTimeMillis();
        String msg = timesamp + path;
        String result = encode(msg, secret);

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://ascendex.com/" + group + "/api/pro/v1/cash/" + path))

                .timeout(Duration.ofMinutes(2))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("x-auth-key", apiKey)
                .header("x-auth-signature", result)
                .header("x-auth-timestamp", String.valueOf(timesamp))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    public static String encode(String message, String secret) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secretKeySpec);
            return new String(Base64.getEncoder().encode(sha256_HMAC.doFinal(message.getBytes())));
        } catch (Exception e) {
            throw new RuntimeException("Unable to sign message.", e);
        }
    }

}