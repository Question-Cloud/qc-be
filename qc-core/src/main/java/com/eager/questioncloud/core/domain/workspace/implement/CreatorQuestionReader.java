package com.eager.questioncloud.core.domain.workspace.implement;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.question.dto.QuestionDto.QuestionInformation;
import com.eager.questioncloud.core.domain.question.model.Question;
import com.eager.questioncloud.core.domain.question.repository.QuestionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatorQuestionReader {
    private final QuestionRepository questionRepository;

    public Question getMyQuestion(Long questionId, Long creatorId) {
        return questionRepository.findByQuestionIdAndCreatorId(questionId, creatorId);
    }

    public List<QuestionInformation> getMyQuestions(Long creatorId, PagingInformation pagingInformation) {
        return questionRepository.findByCreatorIdWithPaging(creatorId, pagingInformation);
    }

    public int countMyQuestions(Long creatorId) {
        return questionRepository.countByCreatorId(creatorId);
    }
}
