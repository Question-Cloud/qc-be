package com.eager.questioncloud.application.business.creator.service

import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.question.dto.QuestionInformation
import com.eager.questioncloud.core.domain.question.event.RegisteredQuestionEvent.Companion.create
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import com.eager.questioncloud.core.domain.question.model.Question.Companion.create
import com.eager.questioncloud.core.domain.question.model.QuestionContent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class CreatorQuestionService(
    private val questionRepository: QuestionRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
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

    fun registerQuestion(creatorId: Long, questionContent: QuestionContent) {
        val question = questionRepository.save(create(creatorId, questionContent))
        applicationEventPublisher.publishEvent(create(question))
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
