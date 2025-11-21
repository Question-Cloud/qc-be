package com.eager.questioncloud.event.consumer

import com.eager.questioncloud.common.event.Event
import com.eager.questioncloud.common.event.MessageListener
import com.eager.questioncloud.common.event.QueueListener
import com.fasterxml.jackson.databind.ObjectMapper
import io.sentry.Sentry
import jakarta.annotation.PreDestroy
import org.springframework.aop.support.AopUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest
import software.amazon.awssdk.services.sqs.model.Message
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Component
@Profile("prod", "local")
class SqsConsumer(
    private val sqsClient: SqsClient,
    private val applicationContext: ApplicationContext,
    private val objectMapper: ObjectMapper
) : ApplicationListener<ApplicationReadyEvent> {
    @Volatile
    private var running = true
    
    private val queueListeners = mutableMapOf<String, QueueListenerInfo>()
    
    @Value("\${AWS_SQS_BASE_URL}")
    private lateinit var sqsBaseUrl: String
    
    private lateinit var executorService: ExecutorService
    
    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        findQueueListeners()
        
        if (queueListeners.isEmpty()) {
            return
        }
        
        executorService = Executors.newFixedThreadPool(queueListeners.size)
        
        queueListeners.forEach { (queueName, listenerInfo) ->
            executorService.submit {
                startConsumer(queueName, listenerInfo)
            }
        }
    }
    
    private fun startConsumer(queueName: String, listenerInfo: QueueListenerInfo) {
        val queueUrl = "$sqsBaseUrl/$queueName"
        
        while (running) {
            try {
                consumeMessages(queueUrl, listenerInfo)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
                break
            } catch (e: Exception) {
                Sentry.captureException(e)
                Thread.sleep(5000)
            }
        }
    }
    
    private fun consumeMessages(queueUrl: String, listenerInfo: QueueListenerInfo) {
        val receiveRequest = ReceiveMessageRequest.builder()
            .queueUrl(queueUrl)
            .maxNumberOfMessages(10)
            .waitTimeSeconds(20)
            .visibilityTimeout(30)
            .build()
        
        val response = sqsClient.receiveMessage(receiveRequest)
        val messages = response.messages()
        
        if (messages.isEmpty()) {
            return
        }
        
        messages.forEach { message ->
            processMessage(queueUrl, message, listenerInfo)
        }
    }
    
    private fun processMessage(queueUrl: String, message: Message, listenerInfo: QueueListenerInfo) {
        try {
            val event = objectMapper.readValue(message.body(), listenerInfo.eventType)
            listenerInfo.onMessage(event)
            deleteMessage(queueUrl, message.receiptHandle())
        } catch (e: Exception) {
            Sentry.captureException(e)
        }
    }
    
    private fun deleteMessage(queueUrl: String, receiptHandle: String) {
        val deleteRequest = DeleteMessageRequest.builder()
            .queueUrl(queueUrl)
            .receiptHandle(receiptHandle)
            .build()
        
        sqsClient.deleteMessage(deleteRequest)
    }
    
    @PreDestroy
    fun shutdown() {
        running = false
        executorService.shutdown()
        
        if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
            executorService.shutdownNow()
        }
        
    }
    
    private fun findQueueListeners() {
        val beans = applicationContext.getBeansWithAnnotation(QueueListener::class.java)
        
        beans.forEach { (beanName, bean) ->
            if (bean is MessageListener<*>) {
                val targetClass = AopUtils.getTargetClass(bean)
                
                val annotation = AnnotationUtils.findAnnotation(targetClass, QueueListener::class.java)
                
                if (annotation != null) {
                    val info = QueueListenerInfo(
                        queueName = annotation.queueName,
                        eventType = annotation.type.java,
                        listener = bean,
                        onMessage = { event: Event ->
                            (bean as MessageListener<Event>).onMessage(event)
                        }
                    )
                    
                    queueListeners[annotation.queueName] = info
                }
            }
        }
    }
    
    data class QueueListenerInfo(
        val queueName: String,
        val eventType: Class<out Event>,
        val listener: MessageListener<*>,
        val onMessage: (event: Event) -> Unit
    )
}