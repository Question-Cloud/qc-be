package com.eager.questioncloud.event.implement

import com.eager.questioncloud.common.message.MessageSender
import com.fasterxml.jackson.databind.ObjectMapper
import io.awspring.cloud.sqs.operations.SqsTemplate
import org.springframework.stereotype.Component

@Component
class SQSMessageSender(
    private val sqsTemplate: SqsTemplate,
    private val objectMapper: ObjectMapper
) : MessageSender {
    override fun sendMessage(payload: Any, to: String, key: String) {
        sqsTemplate.send { t ->
            t.queue(to)
                .messageGroupId(key)
                .messageDeduplicationId(key)
                .payload(objectMapper.writeValueAsString(payload))
        }
    }
}