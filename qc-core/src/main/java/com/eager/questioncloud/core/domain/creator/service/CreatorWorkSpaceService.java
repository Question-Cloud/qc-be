package com.eager.questioncloud.core.domain.creator.service;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.creator.implement.CreatorUpdater;
import com.eager.questioncloud.core.domain.creator.model.Creator;
import com.eager.questioncloud.core.domain.creator.vo.CreatorProfile;
import com.eager.questioncloud.core.domain.post.dto.PostDto.PostListItem;
import com.eager.questioncloud.core.domain.post.implement.PostReader;
import com.eager.questioncloud.core.domain.question.dto.QuestionDto.QuestionInformation;
import com.eager.questioncloud.core.domain.question.implement.QuestionReader;
import com.eager.questioncloud.core.domain.question.implement.QuestionRegister;
import com.eager.questioncloud.core.domain.question.implement.QuestionRemover;
import com.eager.questioncloud.core.domain.question.implement.QuestionUpdater;
import com.eager.questioncloud.core.domain.question.model.Question;
import com.eager.questioncloud.core.domain.question.vo.QuestionContent;
import com.eager.questioncloud.core.domain.review.event.InitReviewStatisticsEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatorWorkSpaceService {
    private final QuestionRegister questionRegister;
    private final QuestionReader questionReader;
    private final QuestionUpdater questionUpdater;
    private final QuestionRemover questionRemover;
    private final PostReader postReader;
    private final CreatorUpdater creatorUpdater;
    private final ApplicationEventPublisher applicationEventPublisher;

    public List<QuestionInformation> getCreatorQuestions(Long creatorId, PagingInformation pagingInformation) {
        return questionReader.getCreatorQuestions(creatorId, pagingInformation);
    }

    public int countCreatorQuestionCount(Long creatorId) {
        return questionReader.countCreatorQuestion(creatorId);
    }

    public QuestionContent getQuestionContent(Long creatorId, Long questionId) {
        Question question = questionReader.findByIdAndCreatorId(questionId, creatorId);
        return question.getQuestionContent();
    }

    public Question registerQuestion(Long creatorId, QuestionContent questionContent) {
        Question question = questionRegister.register(creatorId, questionContent);
        applicationEventPublisher.publishEvent(InitReviewStatisticsEvent.create(question.getId()));
        return question;
    }

    public void modifyQuestion(Long creatorId, Long questionId, QuestionContent questionContent) {
        Question question = questionReader.findByIdAndCreatorId(questionId, creatorId);
        questionUpdater.modifyQuestionContent(question, questionContent);
    }

    public void deleteQuestion(Long creatorId, Long questionId) {
        Question question = questionReader.findByIdAndCreatorId(questionId, creatorId);
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
