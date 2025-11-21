package com.eager.questioncloud.event.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsClient

@Configuration
@Profile("prod", "local")
class SqsConfig {
    @Value("\${spring.cloud.aws.credentials.access-key}")
    private lateinit var awsAccessKey: String
    
    @Value("\${spring.cloud.aws.credentials.secret-key}")
    private lateinit var awsSecretKey: String
    
    @Value("\${spring.cloud.aws.region.static}")
    private lateinit var awsRegion: String
    
    @Bean
    fun sqsClient(): SqsClient {
        return SqsClient.builder()
            .region(Region.of(awsRegion))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(
                        awsAccessKey,
                        awsSecretKey
                    )
                )
            )
            .build()
    }
}