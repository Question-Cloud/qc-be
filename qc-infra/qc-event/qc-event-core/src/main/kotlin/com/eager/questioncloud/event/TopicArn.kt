package com.eager.questioncloud.event

enum class TopicArn(
    val topicArn: String
) {
    QuestionPaymentEvent("arn:aws:sns:ap-northeast-2:503561444273:question-payment"),
    ReviewEvent("arn:aws:sns:ap-northeast-2:503561444273:question-review"),
    SubscribeEvent("arn:aws:sns:ap-northeast-2:503561444273:creator-subscribe")
}