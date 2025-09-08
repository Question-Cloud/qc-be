package com.eager.questioncloud.user.repository

import com.eager.questioncloud.user.domain.EmailVerification
import com.eager.questioncloud.user.enums.EmailVerificationType

interface EmailVerificationRepository {
    fun get(token: String, emailVerificationType: EmailVerificationType): EmailVerification
    
    fun getCreateUserVerification(userId: Long): EmailVerification
    
    fun getForResend(resendToken: String): EmailVerification
    
    fun save(emailVerification: EmailVerification): EmailVerification
    
    fun update(emailVerification: EmailVerification)
}
