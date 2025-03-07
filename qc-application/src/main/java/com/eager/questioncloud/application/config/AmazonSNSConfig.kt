package com.eager.questioncloud.application.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sns.SnsClient

@Configuration
class AmazonSNSConfig(
    @Value("\${spring.cloud.aws.credentials.access-key}") val accessKey: String,
    @Value("\${spring.cloud.aws.credentials.secret-key}") val secretKey: String,
    @Value("\${spring.cloud.aws.region.static}") val region: String
) {
    @Bean
    fun SnsClient(): SnsClient {
        return SnsClient.builder()
            .region(Region.of(region))
            .credentialsProvider {
                AwsBasicCredentials.create(accessKey, secretKey)
            }
            .build()
    }
}
