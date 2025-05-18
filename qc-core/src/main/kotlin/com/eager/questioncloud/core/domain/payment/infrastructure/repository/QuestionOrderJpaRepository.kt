package com.eager.questioncloud.core.domain.payment.infrastructure.repository

import com.eager.questioncloud.core.domain.payment.infrastructure.entity.QuestionOrderEntity
import org.springframework.data.jpa.repository.JpaRepository

interface QuestionOrderJpaRepository : JpaRepository<QuestionOrderEntity, Long>
