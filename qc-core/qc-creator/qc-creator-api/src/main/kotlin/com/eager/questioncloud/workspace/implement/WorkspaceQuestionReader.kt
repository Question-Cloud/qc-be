package com.eager.questioncloud.workspace.implement

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.creator.repository.CreatorRepository
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.workspace.dto.CreatorQuestionInformation
import com.eager.questioncloud.workspace.dto.MyQuestionContent
import org.springframework.stereotype.Component

@Component
class WorkspaceQuestionReader(
    private val creatorRepository: CreatorRepository,
    private val questionQueryAPI: QuestionQueryAPI,
) {
    fun getMyQuestions(userId: Long, pagingInformation: PagingInformation): List<CreatorQuestionInformation> {
        val creator = creatorRepository.findByUserId(userId) ?: throw CoreException(Error.NOT_FOUND)
        return questionQueryAPI.getCreatorQuestions(creator.id, pagingInformation).map {
            CreatorQuestionInformation(
                it.id,
                it.creatorId,
                it.title,
                it.subject,
                it.parentCategory,
                it.childCategory,
                it.thumbnail,
                it.questionLevel,
                it.price,
                it.rate
            )
        }
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
}