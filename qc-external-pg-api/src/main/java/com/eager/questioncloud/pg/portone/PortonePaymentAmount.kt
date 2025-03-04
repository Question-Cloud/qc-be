package com.eager.questioncloud.pg.portone

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class PortonePaymentAmount(
    val total: Int,
    val taxFree: Int,
    val vat: Int,
)
