package com.eager.questioncloud.common.pg

interface PaymentAPI {
    fun getPayment(id: String): PGPayment
    
    fun confirm(pgConfirmRequest: PGConfirmRequest): PGConfirmResponse
    
    fun cancel(pgConfirmRequest: PGConfirmRequest)
}