package com.eager.questioncloud.core.domain.workspace.implement;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.question.common.QuestionFilter;
import com.eager.questioncloud.core.domain.question.model.Question;
import com.eager.questioncloud.core.domain.question.repository.QuestionRepository;
import com.eager.questioncloud.core.domain.workspace.dto.CreatorQuestion;
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

    public List<CreatorQuestion> getMyQuestions(Long creatorId, PagingInformation pagingInformation) {
        List<Question> questions = questionRepository.getQuestionsByFilter(
            new QuestionFilter(null, null, null, null, creatorId, null, pagingInformation));
        return CreatorQuestion.from(questions);
    }

    public int countMyQuestions(Long creatorId) {
        return questionRepository.countByCreatorId(creatorId);
    }
}
