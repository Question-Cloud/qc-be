package com.eager.questioncloud.application.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sns.SnsAsyncClient

@Configuration
class SnsConfig {
    @Value("\${spring.cloud.aws.credentials.access-key}")
    private lateinit var awsAccessKey: String

    @Value("\${spring.cloud.aws.credentials.secret-key}")
    private lateinit var awsSecretKey: String

    @Value("\${spring.cloud.aws.region.static}")
    private lateinit var awsRegion: String

    @Bean
    fun snsAsyncClient(): SnsAsyncClient {
        return SnsAsyncClient.builder()
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