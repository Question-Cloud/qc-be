package com.eager.questioncloud.pg.portone;

import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class PortoneAPI {
    @Value("${PORT_ONE_SECRET_KEY}")
    private String PORT_ONE_SECRET_KEY;

    public PortonePayment getPayment(String paymentId) {
        WebClient webClient = WebClient.create("https://api.portone.io");

        PortonePayment portonePayment = webClient.get()
            .uri("/payments/%s".formatted(paymentId))
            .headers(headers -> headers.set("Authorization", "Portone %s".formatted(PORT_ONE_SECRET_KEY)))
            .exchangeToMono(response -> {
                if (response.statusCode().is2xxSuccessful()) {
                    return response.bodyToMono(PortonePayment.class);
                }
                throw new CustomException(Error.PAYMENT_ERROR);
            })
            .block();

        if (portonePayment == null || portonePayment.getId() == null) {
            throw new CustomException(Error.PAYMENT_ERROR);
        }

        return portonePayment;
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
}