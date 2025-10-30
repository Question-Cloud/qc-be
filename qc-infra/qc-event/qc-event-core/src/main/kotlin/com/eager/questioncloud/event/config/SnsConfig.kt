package com.eager.questioncloud.event.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sns.SnsAsyncClient

@Configuration
@Profile("prod", "local")
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
            .httpClientBuilder(
                NettyNioAsyncHttpClient.builder()
                    .maxConcurrency(200)
            )
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