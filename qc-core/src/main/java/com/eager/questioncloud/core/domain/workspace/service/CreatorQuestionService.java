package com.eager.questioncloud.core.domain.workspace.service;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.question.model.ModifyQuestion;
import com.eager.questioncloud.core.domain.question.model.Question;
import com.eager.questioncloud.core.domain.question.model.RegisterQuestion;
import com.eager.questioncloud.core.domain.question.vo.QuestionContent;
import com.eager.questioncloud.core.domain.review.event.InitReviewStatisticsEvent;
import com.eager.questioncloud.core.domain.workspace.dto.CreatorQuestion;
import com.eager.questioncloud.core.domain.workspace.implement.CreatorQuestionReader;
import com.eager.questioncloud.core.domain.workspace.implement.CreatorQuestionRegister;
import com.eager.questioncloud.core.domain.workspace.implement.CreatorQuestionRemover;
import com.eager.questioncloud.core.domain.workspace.implement.CreatorQuestionUpdater;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatorQuestionService {
    private final CreatorQuestionRegister creatorQuestionRegister;
    private final CreatorQuestionReader creatorQuestionReader;
    private final CreatorQuestionUpdater creatorQuestionUpdater;
    private final CreatorQuestionRemover creatorQuestionRemover;
    private final ApplicationEventPublisher applicationEventPublisher;

    public List<CreatorQuestion> getMyQuestions(Long creatorId, PagingInformation pagingInformation) {
        return creatorQuestionReader.getMyQuestions(creatorId, pagingInformation);
    }

    public int countMyQuestions(Long creatorId) {
        return creatorQuestionReader.countMyQuestions(creatorId);
    }

    public QuestionContent getMyQuestionContent(Long creatorId, Long questionId) {
        Question question = creatorQuestionReader.getMyQuestion(questionId, creatorId);
        return question.getQuestionContent();
    }

    public void registerQuestion(RegisterQuestion registerQuestion) {
        Question question = creatorQuestionRegister.register(registerQuestion);
        applicationEventPublisher.publishEvent(InitReviewStatisticsEvent.create(question.getId()));
    }

    public void modifyQuestion(ModifyQuestion modifyQuestion) {
        creatorQuestionUpdater.modifyQuestionContent(modifyQuestion);
    }

    public void deleteQuestion(Long creatorId, Long questionId) {
        Question question = creatorQuestionReader.getMyQuestion(questionId, creatorId);
        creatorQuestionRemover.delete(question);
    }
}
