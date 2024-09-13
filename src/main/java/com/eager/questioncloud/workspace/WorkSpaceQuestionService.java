package com.eager.questioncloud.workspace;

import com.eager.questioncloud.question.Question;
import com.eager.questioncloud.question.QuestionContent;
import com.eager.questioncloud.question.QuestionDto.QuestionInformationForWorkSpace;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkSpaceQuestionService {
    private final WorkSpaceQuestionRegister workSpaceQuestionRegister;
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
        return workSpaceQuestionRegister.register(creatorId, questionContent);
    }

    public void modify(Long creatorId, Long questionId, QuestionContent questionContent) {
        workSpaceQuestionUpdater.modify(creatorId, questionId, questionContent);
    }

    public void delete(Long creatorId, Long questionId) {
        workSpaceQuestionRemover.remove(creatorId, questionId);
    }
}
