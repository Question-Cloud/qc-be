package com.eager.questioncloud.question.repository

import com.eager.questioncloud.question.entity.PromotionEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PromotionJpaRepository : JpaRepository<PromotionEntity, Long> {
}