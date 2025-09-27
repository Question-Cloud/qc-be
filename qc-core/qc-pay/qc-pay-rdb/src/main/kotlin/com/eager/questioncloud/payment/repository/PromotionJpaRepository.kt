package com.eager.questioncloud.payment.repository

import com.eager.questioncloud.payment.entity.PromotionEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PromotionJpaRepository : JpaRepository<PromotionEntity, Long> {
}