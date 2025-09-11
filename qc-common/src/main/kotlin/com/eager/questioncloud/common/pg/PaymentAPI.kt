package com.eager.questioncloud.common.pg

import com.eager.questioncloud.common.pg.domain.PGPayment

interface PaymentAPI {
    fun getPayment(id: String): PGPayment
    
    fun confirm(pgPayment: PGPayment)
    
    fun cancel(pgPayment: PGPayment)
}