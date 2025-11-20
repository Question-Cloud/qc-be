package com.eager.questioncloud.logging.config

import io.micrometer.observation.ObservationPredicate
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.server.observation.ServerRequestObservationContext

@Configuration
class TracingConfig {
    @Bean
    fun noSpringSecurity(): ObservationPredicate {
        return ObservationPredicate { name, _ -> !name.startsWith("spring.security") }
    }
    
    @Bean
    fun noSchedulingTask(): ObservationPredicate {
        return ObservationPredicate { name, _ -> !name.startsWith("task") }
    }
}

@Configuration
@ConditionalOnClass(ServerRequestObservationContext::class)
class NoActuatorTracingConfig {
    @Bean
    fun noActuator(): ObservationPredicate {
        return ObservationPredicate { name, context ->
            if (context is ServerRequestObservationContext) {
                return@ObservationPredicate !context.carrier.requestURI.startsWith("/actuator")
            }
            return@ObservationPredicate true
        }
    }
}