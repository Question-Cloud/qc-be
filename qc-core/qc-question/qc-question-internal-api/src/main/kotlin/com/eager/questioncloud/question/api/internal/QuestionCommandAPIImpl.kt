package com.eager.questioncloud.question.api.internal

import com.eager.questioncloud.question.domain.Question
import com.eager.questioncloud.question.domain.QuestionContent
import com.eager.questioncloud.question.enums.QuestionLevel
import com.eager.questioncloud.question.enums.QuestionType
import com.eager.questioncloud.question.enums.Subject
import com.eager.questioncloud.question.implement.QuestionMetadataInitializer
import com.eager.questioncloud.question.repository.QuestionRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class QuestionCommandAPIImpl(
    private val questionRepository: QuestionRepository,
    private val questionMetadataInitializer: QuestionMetadataInitializer
) : QuestionCommandAPI {
    @Transactional
    override fun register(registerQuestionAPIRequest: RegisterQuestionAPIRequest): Long {
        val question = questionRepository.save(
            Question.create(
                registerQuestionAPIRequest.creatorId,
                QuestionContent(
                    registerQuestionAPIRequest.questionCategoryId,
                    Subject.valueOf(registerQuestionAPIRequest.subject),
                    registerQuestionAPIRequest.title,
                    registerQuestionAPIRequest.description,
                    registerQuestionAPIRequest.thumbnail,
                    registerQuestionAPIRequest.fileUrl,
                    registerQuestionAPIRequest.explanationUrl,
                    QuestionType.SelfMade,
                    QuestionLevel.valueOf(registerQuestionAPIRequest.questionLevel),
                    registerQuestionAPIRequest.price
                ),
            )
        )
        questionMetadataInitializer.init(question.id)
        
        return question.id
    }
    
    @Transactional
    override fun modify(modifyQuestionAPIRequest: ModifyQuestionAPIRequest) {
        val question =
            questionRepository.findByQuestionIdAndCreatorId(modifyQuestionAPIRequest.questionId, modifyQuestionAPIRequest.creatorId)
        question.modify(
            QuestionContent(
                modifyQuestionAPIRequest.questionCategoryId,
                Subject.valueOf(modifyQuestionAPIRequest.subject),
                modifyQuestionAPIRequest.title,
                modifyQuestionAPIRequest.description,
                modifyQuestionAPIRequest.thumbnail,
                modifyQuestionAPIRequest.fileUrl,
                modifyQuestionAPIRequest.explanationUrl,
                QuestionType.SelfMade,
                QuestionLevel.valueOf(modifyQuestionAPIRequest.questionLevel),
                modifyQuestionAPIRequest.price
            )
        )
        
        questionRepository.save(question)
    }
    
    @Transactional
    override fun delete(questionId: Long, creatorId: Long) {
        val question = questionRepository.findByQuestionIdAndCreatorId(questionId, creatorId)
        question.delete()
        questionRepository.save(question)
    }
}