package com.eager.questioncloud.workspace.service;

import com.eager.questioncloud.question.domain.Question;
import com.eager.questioncloud.question.dto.QuestionContent;
import com.eager.questioncloud.question.dto.QuestionDto.QuestionInformationForWorkSpace;
import com.eager.questioncloud.workspace.implement.WorkSpaceQuestionAppender;
import com.eager.questioncloud.workspace.implement.WorkSpaceQuestionReader;
import com.eager.questioncloud.workspace.implement.WorkSpaceQuestionRemover;
import com.eager.questioncloud.workspace.implement.WorkSpaceQuestionUpdater;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkSpaceQuestionService {
    private final WorkSpaceQuestionAppender workSpaceQuestionAppender;
    private final WorkSpaceQuestionUpdater workSpaceQuestionUpdater;
    private final WorkSpaceQuestionRemover workSpaceQuestionRemover;
    private final WorkSpaceQuestionReader workSpaceQuestionReader;

    public int count(Long creatorId) {
        return workSpaceQuestionReader.count(creatorId);
    }

    public List<QuestionInformationForWorkSpace> getQuestions(Long creatorId, Pageable pageable) {
        return workSpaceQuestionReader.getQuestions(creatorId, pageable);
    }

    public QuestionContent getQuestionContent(Long creatorId, Long questionId) {
        return workSpaceQuestionReader.getQuestionContent(creatorId, questionId);
    }

    public Question register(Long creatorId, QuestionContent questionContent) {
        return workSpaceQuestionAppender.register(creatorId, questionContent);
    }

    public void modify(Long creatorId, Long questionId, QuestionContent questionContent) {
        workSpaceQuestionUpdater.modify(creatorId, questionId, questionContent);
    }

    public void delete(Long creatorId, Long questionId) {
        workSpaceQuestionRemover.remove(creatorId, questionId);
    }
}
