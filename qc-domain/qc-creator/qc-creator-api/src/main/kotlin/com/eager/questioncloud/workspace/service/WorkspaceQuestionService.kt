package com.eager.questioncloud.workspace.service

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.creator.infrastructure.repository.CreatorRepository
import com.eager.questioncloud.question.api.internal.*
import com.eager.questioncloud.workspace.dto.MyQuestionContent
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class WorkspaceQuestionService(
    private val creatorRepository: CreatorRepository,
    private val questionQueryAPI: QuestionQueryAPI,
    private val questionCommandAPI: QuestionCommandAPI,
) {
    fun getMyQuestions(userId: Long, pagingInformation: PagingInformation): List<QuestionInformationQueryResult> {
        val creator = creatorRepository.findByUserId(userId) ?: throw CoreException(Error.NOT_FOUND)
        return questionQueryAPI.getCreatorQuestions(creator.id, pagingInformation)
    }

    fun countMyQuestions(userId: Long): Int {
        val creator = creatorRepository.findByUserId(userId) ?: throw CoreException(Error.NOT_FOUND)
        return questionQueryAPI.countByCreatorId(creator.id)
    }

    fun getMyQuestionContent(userId: Long, questionId: Long): MyQuestionContent {
        val creator = creatorRepository.findByUserId(userId) ?: throw CoreException(Error.NOT_FOUND)
        val questionContent = questionQueryAPI.getQuestionContent(questionId, creator.id)
        return MyQuestionContent(
            questionContent.questionCategoryId,
            questionContent.subject,
            questionContent.title,
            questionContent.description,
            questionContent.thumbnail,
            questionContent.fileUrl,
            questionContent.explanationUrl,
            questionContent.questionLevel,
            questionContent.price
        )
    }

    @Transactional
    fun registerQuestion(userId: Long, command: RegisterQuestionCommand) {
        val creator = creatorRepository.findByUserId(userId) ?: throw CoreException(Error.NOT_FOUND)
        questionCommandAPI.register(creator.id, command)
    }

    fun modifyQuestion(userId: Long, questionId: Long, command: ModifyQuestionCommand) {
        val creator = creatorRepository.findByUserId(userId) ?: throw CoreException(Error.NOT_FOUND)
        questionCommandAPI.modify(creator.id, command)
    }

    fun deleteQuestion(userId: Long, questionId: Long) {
        val creator = creatorRepository.findByUserId(userId) ?: throw CoreException(Error.NOT_FOUND)
        questionCommandAPI.delete(questionId, creator.id)
    }
}
