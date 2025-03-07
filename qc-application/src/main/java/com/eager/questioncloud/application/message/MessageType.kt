package com.eager.questioncloud.application.message

enum class MessageType(val queueName: String) {
    FAIL_CHARGE_POINT("fail.charge.point"), FAIL_QUESTION_PAYMENT("fail.question.payment"), DELAY_QUEUE("delay")
}
