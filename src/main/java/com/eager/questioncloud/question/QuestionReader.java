package com.eager.questioncloud.question;

import com.eager.questioncloud.creator.CreatorReader;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.question.QuestionDto.QuestionInformation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionReader {
    private final QuestionRepository questionRepository;
    private final CreatorReader creatorReader;

    public int getTotalFiltering(QuestionFilter questionFilter) {
        return questionRepository.getTotalFiltering(questionFilter);
    }

    public List<QuestionInformation> getQuestionListByFiltering(QuestionFilter questionFilter) {
        return questionRepository.getQuestionListByFiltering(questionFilter);
    }

    public QuestionInformation getQuestionInformation(Long questionId, Long userId) {
        return questionRepository.getQuestionInformation(questionId, userId);
    }

    public List<Question> getQuestions(List<Long> questionIds) {
        return questionRepository.getQuestionListInIds(questionIds);
    }

    public Boolean isAvailable(Long questionId) {
        return questionRepository.isAvailable(questionId);
    }

    public Question get(Long questionId) {
        return questionRepository.get(questionId);
    }

    public QuestionContent getQuestionContent(Long creatorId, Long questionId) {
        Question question = questionRepository.get(questionId);
        if (!question.getCreatorId().equals(creatorId)) {
            throw new CustomException(Error.FORBIDDEN);
        }
        return QuestionContent.of(question);
    }
}
