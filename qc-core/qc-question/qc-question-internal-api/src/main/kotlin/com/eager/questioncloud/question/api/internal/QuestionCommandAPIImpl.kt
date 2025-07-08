package com.eager.questioncloud.question.api.internal

import com.eager.questioncloud.question.domain.Question
import com.eager.questioncloud.question.domain.QuestionContent
import com.eager.questioncloud.question.enums.QuestionLevel
import com.eager.questioncloud.question.enums.QuestionType
import com.eager.questioncloud.question.enums.Subject
import com.eager.questioncloud.question.implement.QuestionMetadataInitializer
import com.eager.questioncloud.question.infrastructure.repository.QuestionRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class QuestionCommandAPIImpl(
    private val questionRepository: QuestionRepository,
    private val questionMetadataInitializer: QuestionMetadataInitializer
) : QuestionCommandAPI {
    @Transactional
    override fun register(creatorId: Long, command: RegisterQuestionCommand): Long {
        val question = questionRepository.save(
            Question.create(
                creatorId,
                QuestionContent(
                    command.questionCategoryId,
                    Subject.valueOf(command.subject),
                    command.title,
                    command.description,
                    command.thumbnail,
                    command.fileUrl,
                    command.explanationUrl,
                    QuestionType.SelfMade,
                    QuestionLevel.valueOf(command.questionLevel),
                    command.price
                ),
            )
        )
        questionMetadataInitializer.init(question.id)

        return question.id
    }

    override fun modify(questionId: Long, command: ModifyQuestionCommand) {
        val question = questionRepository.get(questionId)
        question.modify(
            QuestionContent(
                command.questionCategoryId,
                Subject.valueOf(command.subject),
                command.title,
                command.description,
                command.thumbnail,
                command.fileUrl,
                command.explanationUrl,
                QuestionType.SelfMade,
                QuestionLevel.valueOf(command.questionLevel),
                command.price
            )
        )

        questionRepository.save(question)
    }

    override fun delete(questionId: Long, creatorId: Long) {
        val question = questionRepository.findByQuestionIdAndCreatorId(questionId, creatorId)
        question.delete()
        questionRepository.save(question)
    }
}