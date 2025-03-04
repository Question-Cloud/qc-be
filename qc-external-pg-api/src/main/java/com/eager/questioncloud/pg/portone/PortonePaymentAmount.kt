package com.eager.questioncloud.pg.portone;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PortonePaymentAmount {
    private int total;
    private int taxFree;
    private int vat;
}
