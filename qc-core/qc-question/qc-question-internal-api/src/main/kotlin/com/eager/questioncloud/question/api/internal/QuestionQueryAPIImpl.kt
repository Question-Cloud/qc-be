package com.eager.questioncloud.question.api.internal

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.question.repository.QuestionRepository
import com.eager.questioncloud.question.repository.UserQuestionRepository
import org.springframework.stereotype.Component

@Component
class QuestionQueryAPIImpl(
    private val questionRepository: QuestionRepository,
    private val userQuestionRepository: UserQuestionRepository
) : QuestionQueryAPI {
    override fun getQuestionInformation(questionId: Long): QuestionInformationQueryResult {
        val question = questionRepository.getQuestionInformation(questionId)
        
        return QuestionInformationQueryResult(
            question.id,
            question.creatorId,
            question.title,
            question.subject.value,
            question.parentCategory,
            question.childCategory,
            question.thumbnail,
            question.questionLevel.value,
            question.price,
            question.rate
        )
    }
    
    override fun getQuestionInformation(questionIds: List<Long>): List<QuestionInformationQueryResult> {
        val questions = questionRepository.findByQuestionIdIn(questionIds)
        return questions.map {
            QuestionInformationQueryResult(
                it.id,
                it.creatorId,
                it.title,
                it.subject.value,
                it.parentCategory,
                it.childCategory,
                it.thumbnail,
                it.questionLevel.value,
                it.price,
                it.rate
            )
        }
    }
    
    override fun isAvailable(questionId: Long): Boolean {
        return questionRepository.isAvailable(questionId)
    }
    
    override fun isOwned(userId: Long, questionId: Long): Boolean {
        return userQuestionRepository.isOwned(userId, questionId)
    }
    
    override fun isOwned(userId: Long, questionIds: List<Long>): Boolean {
        return userQuestionRepository.isOwned(userId, questionIds)
    }
    
    override fun getCreatorQuestions(
        creatorId: Long,
        pagingInformation: PagingInformation
    ): List<QuestionInformationQueryResult> {
        val results = questionRepository.getQuestionInformationByCreatorIdWithPaging(creatorId, pagingInformation)
        return results.map {
            QuestionInformationQueryResult(
                it.id,
                it.creatorId,
                it.title,
                it.subject.value,
                it.parentCategory,
                it.childCategory,
                it.thumbnail,
                it.questionLevel.value,
                it.price,
                it.rate
            )
        }
    }
    
    override fun countByCreatorId(creatorId: Long): Int {
        return questionRepository.countByCreatorId(creatorId)
    }
    
    override fun getQuestionContent(questionId: Long, creatorId: Long): QuestionContentQueryResult {
        val question = questionRepository.findByQuestionIdAndCreatorId(questionId, creatorId)
        
        return QuestionContentQueryResult(
            question.questionContent.questionCategoryId,
            question.questionContent.subject.value,
            question.questionContent.title,
            question.questionContent.description,
            question.questionContent.thumbnail,
            question.questionContent.fileUrl,
            question.questionContent.explanationUrl,
            question.questionContent.questionLevel.value,
            question.questionContent.price,
        )
    }
}