package com.eager.questioncloud.pg.portone;

import com.eager.questioncloud.pg.exception.InvalidPaymentIdException;
import com.eager.questioncloud.pg.exception.PGException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class PortoneAPI {
    @Value("${PORT_ONE_SECRET_KEY}")
    private String PORT_ONE_SECRET_KEY;
    private static final String BASE_URL = "https://api.portone.io";
    private OkHttpClient client;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        client = new OkHttpClient()
            .newBuilder()
            .callTimeout(5, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build();
    }

    public PortonePayment getPayment(String paymentId) {
        Request request = new Request.Builder()
            .url(String.format("%s/payments/%s", BASE_URL, paymentId))
            .header("Authorization", "Portone " + PORT_ONE_SECRET_KEY)
            .get()
            .build();

        try (Response response = client.newCall(request).execute()) {
            validatePaymentResponse(response);
            return objectMapper.readValue(response.body().string(), PortonePayment.class);
        } catch (InvalidPaymentIdException e) {
            throw e;
        } catch (Exception e) {
            throw new PGException();
        }
    }

    public void cancel(String paymentId) {
        WebClient webClient = WebClient.create("https://api.portone.io");
        webClient.post()
            .uri("/payments/%s/cancel".formatted(paymentId))
            .headers(headers -> headers.set("Authorization", "Portone %s".formatted(PORT_ONE_SECRET_KEY)))
            .body(BodyInserters.fromValue(new PortoneCancelRequest("Invalid Payment")))
            .exchangeToMono(response -> response.bodyToMono(Void.class))
            .subscribe();
    }

    private void validatePaymentResponse(Response response) {
        if (response.code() == 200) {
            return;
        }

        if (response.code() == 404) {
            throw new InvalidPaymentIdException();
        }

        if (response.body() == null) {
            throw new PGException();
        }
    }
}