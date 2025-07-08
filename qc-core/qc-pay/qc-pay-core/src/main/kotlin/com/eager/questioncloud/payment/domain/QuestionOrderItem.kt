package com.eager.questioncloud.payment.domain

class QuestionOrderItem(
    var id: Long = 0,
    var questionId: Long,
    var price: Int,
)