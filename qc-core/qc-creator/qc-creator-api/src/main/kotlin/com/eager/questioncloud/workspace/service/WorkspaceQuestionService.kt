package com.eager.questioncloud.workspace.service

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.creator.repository.CreatorRepository
import com.eager.questioncloud.question.api.internal.ModifyQuestionCommand
import com.eager.questioncloud.question.api.internal.QuestionCommandAPI
import com.eager.questioncloud.question.api.internal.RegisterQuestionCommand
import com.eager.questioncloud.workspace.dto.CreatorQuestionInformation
import com.eager.questioncloud.workspace.dto.MyQuestionContent
import com.eager.questioncloud.workspace.implement.WorkspaceQuestionReader
import org.springframework.stereotype.Component

@Component
class WorkspaceQuestionService(
    private val workspaceQuestionReader: WorkspaceQuestionReader,
    private val creatorRepository: CreatorRepository,
    private val questionCommandAPI: QuestionCommandAPI,
) {
    fun getMyQuestions(userId: Long, pagingInformation: PagingInformation): List<CreatorQuestionInformation> {
        return workspaceQuestionReader.getMyQuestions(userId, pagingInformation)
    }
    
    fun countMyQuestions(userId: Long): Int {
        return workspaceQuestionReader.countMyQuestions(userId)
    }
    
    fun getMyQuestionContent(userId: Long, questionId: Long): MyQuestionContent {
        return workspaceQuestionReader.getMyQuestionContent(userId, questionId)
    }
    
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
