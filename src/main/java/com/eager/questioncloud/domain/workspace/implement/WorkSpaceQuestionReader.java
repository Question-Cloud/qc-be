package com.eager.questioncloud.domain.workspace.implement;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.domain.question.dto.QuestionDto.QuestionInformationForWorkSpace;
import com.eager.questioncloud.domain.question.model.Question;
import com.eager.questioncloud.domain.question.repository.QuestionRepository;
import com.eager.questioncloud.domain.question.vo.QuestionContent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkSpaceQuestionReader {
    private final QuestionRepository questionRepository;

    public int count(Long creatorId) {
        return questionRepository.countCreatorQuestion(creatorId);
    }

    public List<QuestionInformationForWorkSpace> getQuestions(Long creatorId, Pageable pageable) {
        return questionRepository.getCreatorQuestion(creatorId, pageable);
    }

    public QuestionContent getQuestionContent(Long creatorId, Long questionId) {
        Question question = questionRepository.get(questionId);

        if (!isCreator(question, creatorId)) {
            throw new CustomException(Error.FORBIDDEN);
        }

        return question.getQuestionContent();
    }

    private Boolean isCreator(Question question, Long creatorId) {
        return question.getCreatorId().equals(creatorId);
    }
}
