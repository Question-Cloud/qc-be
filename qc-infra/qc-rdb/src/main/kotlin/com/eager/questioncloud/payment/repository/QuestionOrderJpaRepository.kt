package com.eager.questioncloud.payment.repository

import com.eager.questioncloud.payment.entity.QuestionOrderEntity
import org.springframework.data.jpa.repository.JpaRepository

interface QuestionOrderJpaRepository : JpaRepository<QuestionOrderEntity, Long>
