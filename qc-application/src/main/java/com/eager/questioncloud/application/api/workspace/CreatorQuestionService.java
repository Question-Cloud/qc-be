package com.eager.questioncloud.application.api.workspace;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.question.Question;
import com.eager.questioncloud.core.domain.question.QuestionContent;
import com.eager.questioncloud.core.domain.question.QuestionInformation;
import com.eager.questioncloud.core.domain.question.QuestionRepository;
import com.eager.questioncloud.core.domain.question.RegisteredQuestionEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatorQuestionService {
    private final QuestionRepository questionRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public List<QuestionInformation> getMyQuestions(Long creatorId, PagingInformation pagingInformation) {
        return questionRepository.findByCreatorIdWithPaging(creatorId, pagingInformation);
    }

    public int countMyQuestions(Long creatorId) {
        return questionRepository.countByCreatorId(creatorId);
    }

    public QuestionContent getMyQuestionContent(Long creatorId, Long questionId) {
        Question question = questionRepository.findByQuestionIdAndCreatorId(questionId, creatorId);
        return question.getQuestionContent();
    }

    public void registerQuestion(Long creatorId, QuestionContent questionContent) {
        Question question = questionRepository.save(Question.create(creatorId, questionContent));
        applicationEventPublisher.publishEvent(RegisteredQuestionEvent.create(question));
    }

    public void modifyQuestion(Long creatorId, Long questionId, QuestionContent questionContent) {
        Question question = questionRepository.findByQuestionIdAndCreatorId(questionId, creatorId);
        question.modify(questionContent);
        questionRepository.save(question);
    }

    public void deleteQuestion(Long creatorId, Long questionId) {
        Question question = questionRepository.findByQuestionIdAndCreatorId(questionId, creatorId);
        question.delete();
        questionRepository.save(question);
    }
}
