package com.eager.questioncloud.workspace.service

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.workspace.command.DeleteQuestionCommand
import com.eager.questioncloud.workspace.command.ModifyQuestionCommand
import com.eager.questioncloud.workspace.command.RegisterQuestionCommand
import com.eager.questioncloud.workspace.dto.CreatorQuestionInformation
import com.eager.questioncloud.workspace.dto.MyQuestionContent
import com.eager.questioncloud.workspace.implement.WorkspaceQuestionReader
import com.eager.questioncloud.workspace.implement.WorkspaceQuestionRegister
import com.eager.questioncloud.workspace.implement.WorkspaceQuestionRemover
import com.eager.questioncloud.workspace.implement.WorkspaceQuestionUpdater
import org.springframework.stereotype.Component

@Component
class WorkspaceQuestionService(
    private val workspaceQuestionReader: WorkspaceQuestionReader,
    private val workspaceQuestionRegister: WorkspaceQuestionRegister,
    private val workspaceQuestionUpdater: WorkspaceQuestionUpdater,
    private val workspaceQuestionRemover: WorkspaceQuestionRemover,
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
    
    fun registerQuestion(command: RegisterQuestionCommand) {
        workspaceQuestionRegister.registerQuestion(command)
    }
    
    fun modifyQuestion(command: ModifyQuestionCommand) {
        workspaceQuestionUpdater.modifyQuestion(command)
    }
    
    fun deleteQuestion(command: DeleteQuestionCommand) {
        workspaceQuestionRemover.deleteQuestion(command)
    }
}
