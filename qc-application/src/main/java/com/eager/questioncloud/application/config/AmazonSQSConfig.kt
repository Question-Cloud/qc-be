package com.eager.questioncloud.application.config

import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsAsyncClient

@Configuration
class AmazonSQSConfig(
    @Value("\${spring.cloud.aws.credentials.access-key}") val accessKey: String,
    @Value("\${spring.cloud.aws.credentials.secret-key}") val secretKey: String,
    @Value("\${spring.cloud.aws.region.static}") val region: String
) {
    @Bean
    fun sqsAsyncClient(): SqsAsyncClient {
        return SqsAsyncClient.builder()
            .credentialsProvider {
                AwsBasicCredentials.create(accessKey, secretKey)
            }
            .region(Region.of(region))
            .build()
    }

    @Bean
    fun defaultSqsListenerContainerFactory(): SqsMessageListenerContainerFactory<Any> {
        return SqsMessageListenerContainerFactory
            .builder<Any>()
            .sqsAsyncClient(sqsAsyncClient())
            .build()
    }
}