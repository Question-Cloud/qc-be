package com.eager.questioncloud.question;

import com.eager.questioncloud.creator.Creator;
import com.eager.questioncloud.creator.CreatorReader;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.question.QuestionDto.QuestionInformation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatorQuestionReader {
    private final CreatorReader creatorReader;
    private final QuestionRepository questionRepository;

    public int count(Long userId) {
        Creator creator = creatorReader.getByUserId(userId);
        return questionRepository.getTotalFiltering(
            new QuestionFilter(userId, null, null, null, creator.getId(), QuestionSortType.Latest, null));
    }

    public List<QuestionInformation> getQuestions(Long userId, Pageable pageable) {
        Creator creator = creatorReader.getByUserId(userId);
        return questionRepository.getQuestionListByFiltering(
            new QuestionFilter(userId, null, null, null, creator.getId(), QuestionSortType.Latest, pageable)
        );
    }

    public QuestionContent getQuestionContent(Long userId, Long questionId) {
        Creator creator = creatorReader.getByUserId(userId);
        Question question = questionRepository.get(questionId);

        if (!isCreator(question, creator)) {
            throw new CustomException(Error.FORBIDDEN);
        }

        return QuestionContent.of(question);
    }

    private Boolean isCreator(Question question, Creator creator) {
        return question.getCreatorId().equals(creator.getId());
    }
}
