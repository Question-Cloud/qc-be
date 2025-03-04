package com.eager.questioncloud.pg.portone;

import com.eager.questioncloud.pg.exception.InvalidPaymentIdException;
import com.eager.questioncloud.pg.exception.PGException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PortoneAPI {
    @Value("${PORT_ONE_SECRET_KEY}")
    private String PORT_ONE_SECRET_KEY;
    private static final String BASE_URL = "https://api.portone.io";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final OkHttpClient client = new OkHttpClient().newBuilder()
        .callTimeout(5, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build();

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
        Request request = new Request.Builder()
            .url(String.format("%s/payments/%s/cancel", BASE_URL, paymentId))
            .header("Authorization", "Portone " + PORT_ONE_SECRET_KEY)
            .post(getCancelRequestBody())
            .build();

        try (Response response = client.newCall(request).execute()) {
            validateCancelResponse(response);
        } catch (Exception e) {
            throw new PGException();
        }
    }

    private RequestBody getCancelRequestBody() {
        try {
            return RequestBody.create(
                objectMapper.writeValueAsString(new PortoneCancelRequest("Invalid Payment")),
                MediaType.parse("application/json; charset=utf-8"));
        } catch (Exception e) {
            throw new PGException();
        }
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

    private void validateCancelResponse(Response response) {
        if (response.code() == 200) {
            return;
        }
        throw new PGException();
    }
}