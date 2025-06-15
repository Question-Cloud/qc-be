package com.eager.questioncloud.config

import io.micrometer.observation.ObservationPredicate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.server.observation.ServerRequestObservationContext

@Configuration
class TracingConfig {
    @Bean
    fun noActuator(): ObservationPredicate {
        return ObservationPredicate { _, context ->
            if (context is ServerRequestObservationContext) {
                return@ObservationPredicate !context.carrier.requestURI.startsWith("/actuator")
            }
            return@ObservationPredicate true
        }
    }

    @Bean
    fun noSpringSecurity(): ObservationPredicate {
        return ObservationPredicate { name, _ -> !name.startsWith("spring.security") }
    }
}