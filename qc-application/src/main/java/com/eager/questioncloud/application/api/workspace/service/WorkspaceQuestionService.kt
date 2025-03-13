package com.eager.questioncloud.application.api.workspace.service

import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.question.dto.QuestionInformation
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.core.domain.question.model.Question.Companion.create
import com.eager.questioncloud.core.domain.question.model.QuestionContent
import com.eager.questioncloud.core.domain.review.implement.QuestionReviewStatisticsGenerator
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class WorkspaceQuestionService(
    private val questionRepository: QuestionRepository,
    private val questionReviewStatisticsGenerator: QuestionReviewStatisticsGenerator
) {
    fun getMyQuestions(creatorId: Long, pagingInformation: PagingInformation): List<QuestionInformation> {
        return questionRepository.findByCreatorIdWithPaging(creatorId, pagingInformation)
    }

    fun countMyQuestions(creatorId: Long): Int {
        return questionRepository.countByCreatorId(creatorId)
    }

    fun getMyQuestionContent(creatorId: Long, questionId: Long): QuestionContent {
        val question = questionRepository.findByQuestionIdAndCreatorId(questionId, creatorId)
        return question.questionContent
    }

    @Transactional
    fun registerQuestion(creatorId: Long, questionContent: QuestionContent) {
        val question = questionRepository.save(create(creatorId, questionContent))
        questionReviewStatisticsGenerator.generate(question.id!!)
    }

    fun modifyQuestion(creatorId: Long, questionId: Long, questionContent: QuestionContent) {
        val question = questionRepository.findByQuestionIdAndCreatorId(questionId, creatorId)
        question.modify(questionContent)
        questionRepository.save(question)
    }

    fun deleteQuestion(creatorId: Long, questionId: Long) {
        val question = questionRepository.findByQuestionIdAndCreatorId(questionId, creatorId)
        question.delete()
        questionRepository.save(question)
    }
}
