package com.eager.questioncloud.payment.infrastructure.repository

import com.eager.questioncloud.payment.infrastructure.entity.QuestionOrderEntity
import org.springframework.data.jpa.repository.JpaRepository

interface QuestionOrderJpaRepository : JpaRepository<QuestionOrderEntity, Long>
