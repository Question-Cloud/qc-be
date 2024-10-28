package com.eager.questioncloud.core.domain.creator.service;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.board.dto.QuestionBoardDto.QuestionBoardListItem;
import com.eager.questioncloud.core.domain.board.implement.QuestionBoardReader;
import com.eager.questioncloud.core.domain.question.dto.QuestionDto.QuestionInformation;
import com.eager.questioncloud.core.domain.question.implement.QuestionAppender;
import com.eager.questioncloud.core.domain.question.implement.QuestionReader;
import com.eager.questioncloud.core.domain.question.implement.QuestionRemover;
import com.eager.questioncloud.core.domain.question.implement.QuestionUpdater;
import com.eager.questioncloud.core.domain.question.model.Question;
import com.eager.questioncloud.core.domain.question.vo.QuestionContent;
import com.eager.questioncloud.core.domain.review.implement.QuestionReviewStatisticsAppender;
import com.eager.questioncloud.core.domain.review.model.QuestionReviewStatistics;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatorWorkSpaceService {
    private final QuestionAppender questionAppender;
    private final QuestionReader questionReader;
    private final QuestionUpdater questionUpdater;
    private final QuestionRemover questionRemover;
    private final QuestionReviewStatisticsAppender questionReviewStatisticsAppender;
    private final QuestionBoardReader questionBoardReader;

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
        Question question = questionAppender.append(Question.create(creatorId, questionContent));
        questionReviewStatisticsAppender.append(QuestionReviewStatistics.create(question.getId()));
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

    public List<QuestionBoardListItem> getCreatorQuestionBoardList(Long creatorId, PagingInformation pagingInformation) {
        return questionBoardReader.getCreatorQuestionBoardList(creatorId, pagingInformation);
    }

    public int countCreatorQuestionBoardList(Long creatorId) {
        return questionBoardReader.countCreatorQuestionBoard(creatorId);
    }
}
