package com.eager.questioncloud.pg

import com.eager.questioncloud.pg.model.PGPayment

interface PaymentAPI {
    fun getPayment(id: String): PGPayment
    
    fun confirm(pgPayment: PGPayment)
    
    fun cancel(pgPayment: PGPayment)
}