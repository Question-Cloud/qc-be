package com.eager.questioncloud.workspace.implement

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.question.api.internal.QuestionQueryAPI
import com.eager.questioncloud.workspace.dto.CreatorQuestionInformation
import com.eager.questioncloud.workspace.dto.MyQuestionContent
import org.springframework.stereotype.Component

@Component
class WorkspaceQuestionReader(
    private val questionQueryAPI: QuestionQueryAPI,
) {
    fun getMyQuestions(creatorId: Long, pagingInformation: PagingInformation): List<CreatorQuestionInformation> {
        return questionQueryAPI.getCreatorQuestions(creatorId, pagingInformation).map {
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
    
    fun countMyQuestions(creatorId: Long): Int {
        return questionQueryAPI.countByCreatorId(creatorId)
    }
    
    fun getMyQuestionContent(creatorId: Long, questionId: Long): MyQuestionContent {
        val questionContent = questionQueryAPI.getQuestionContent(questionId, creatorId)
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