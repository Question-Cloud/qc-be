package com.eager.questioncloud.core.domain.workspace.service;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.creator.implement.CreatorUpdater;
import com.eager.questioncloud.core.domain.creator.model.Creator;
import com.eager.questioncloud.core.domain.creator.vo.CreatorProfile;
import com.eager.questioncloud.core.domain.post.dto.PostDto.PostListItem;
import com.eager.questioncloud.core.domain.post.implement.PostReader;
import com.eager.questioncloud.core.domain.question.dto.QuestionDto.QuestionInformation;
import com.eager.questioncloud.core.domain.question.implement.QuestionRemover;
import com.eager.questioncloud.core.domain.question.implement.QuestionUpdater;
import com.eager.questioncloud.core.domain.question.model.Question;
import com.eager.questioncloud.core.domain.question.vo.QuestionContent;
import com.eager.questioncloud.core.domain.review.event.InitReviewStatisticsEvent;
import com.eager.questioncloud.core.domain.workspace.implement.CreatorQuestionReader;
import com.eager.questioncloud.core.domain.workspace.implement.CreatorQuestionRegister;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatorWorkSpaceService {
    private final CreatorQuestionRegister creatorQuestionRegister;
    private final CreatorQuestionReader creatorQuestionReader;
    private final QuestionUpdater questionUpdater;
    private final QuestionRemover questionRemover;
    private final PostReader postReader;
    private final CreatorUpdater creatorUpdater;
    private final ApplicationEventPublisher applicationEventPublisher;

    public List<QuestionInformation> getCreatorQuestions(Long creatorId, PagingInformation pagingInformation) {
        return creatorQuestionReader.getMyQuestions(creatorId, pagingInformation);
    }

    public int countCreatorQuestionCount(Long creatorId) {
        return creatorQuestionReader.countMyQuestions(creatorId);
    }

    public QuestionContent getQuestionContent(Long creatorId, Long questionId) {
        Question question = creatorQuestionReader.getMyQuestion(questionId, creatorId);
        return question.getQuestionContent();
    }

    public void registerQuestion(Long creatorId, QuestionContent questionContent) {
        Question question = creatorQuestionRegister.register(Question.create(creatorId, questionContent));
        applicationEventPublisher.publishEvent(InitReviewStatisticsEvent.create(question.getId()));
    }

    public void modifyQuestion(Long creatorId, Long questionId, QuestionContent questionContent) {
        Question question = creatorQuestionReader.getMyQuestion(questionId, creatorId);
        questionUpdater.modifyQuestionContent(question, questionContent);
    }

    public void deleteQuestion(Long creatorId, Long questionId) {
        Question question = creatorQuestionReader.getMyQuestion(questionId, creatorId);
        questionRemover.delete(question);
    }

    public List<PostListItem> getCreatorQuestionBoardList(Long creatorId, PagingInformation pagingInformation) {
        return postReader.getCreatorPostList(creatorId, pagingInformation);
    }

    public int countCreatorQuestionBoardList(Long creatorId) {
        return postReader.countCreatorPost(creatorId);
    }

    public void updateCreatorProfile(Creator creator, CreatorProfile creatorProfile) {
        creatorUpdater.updateCreatorProfile(creator, creatorProfile);
    }
}
