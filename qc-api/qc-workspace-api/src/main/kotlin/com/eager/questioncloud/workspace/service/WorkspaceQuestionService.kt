package com.eager.questioncloud.workspace.service

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.question.domain.Question
import com.eager.questioncloud.question.domain.QuestionContent
import com.eager.questioncloud.question.dto.QuestionInformation
import com.eager.questioncloud.question.implement.QuestionMetadataInitializer
import com.eager.questioncloud.question.infrastructure.repository.QuestionRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class WorkspaceQuestionService(
    private val creatorRepository: CreatorRepository,
    private val questionRepository: QuestionRepository,
    private val questionMetadataInitializer: QuestionMetadataInitializer
) {
    fun getMyQuestions(userId: Long, pagingInformation: PagingInformation): List<QuestionInformation> {
        val creator = creatorRepository.findByUserId(userId) ?: throw CoreException(Error.NOT_FOUND)
        return questionRepository.getQuestionInformationByCreatorIdWithPaging(creator.id, pagingInformation)
    }

    fun countMyQuestions(userId: Long): Int {
        val creator = creatorRepository.findByUserId(userId) ?: throw CoreException(Error.NOT_FOUND)
        return questionRepository.countByCreatorId(creator.id)
    }

    fun getMyQuestionContent(userId: Long, questionId: Long): QuestionContent {
        val creator = creatorRepository.findByUserId(userId) ?: throw CoreException(Error.NOT_FOUND)
        val question = questionRepository.findByQuestionIdAndCreatorId(questionId, creator.id)
        return question.questionContent
    }

    @Transactional
    fun registerQuestion(userId: Long, questionContent: QuestionContent) {
        val creator = creatorRepository.findByUserId(userId) ?: throw CoreException(Error.NOT_FOUND)
        val question = questionRepository.save(Question.create(creator.id, questionContent))
        questionMetadataInitializer.init(question.id)
    }

    fun modifyQuestion(userId: Long, questionId: Long, questionContent: QuestionContent) {
        val creator = creatorRepository.findByUserId(userId) ?: throw CoreException(Error.NOT_FOUND)
        val question = questionRepository.findByQuestionIdAndCreatorId(questionId, creator.id)
        question.modify(questionContent)
        questionRepository.save(question)
    }

    fun deleteQuestion(userId: Long, questionId: Long) {
        val creator = creatorRepository.findByUserId(userId) ?: throw CoreException(Error.NOT_FOUND)
        val question = questionRepository.findByQuestionIdAndCreatorId(questionId, creator.id)
        question.delete()
        questionRepository.save(question)
    }
}
