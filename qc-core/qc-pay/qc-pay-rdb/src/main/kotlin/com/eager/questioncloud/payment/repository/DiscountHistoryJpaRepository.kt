package com.eager.questioncloud.payment.repository

import com.eager.questioncloud.payment.entity.DiscountHistoryEntity
import org.springframework.data.jpa.repository.JpaRepository

interface DiscountHistoryJpaRepository : JpaRepository<DiscountHistoryEntity, Long> {
}