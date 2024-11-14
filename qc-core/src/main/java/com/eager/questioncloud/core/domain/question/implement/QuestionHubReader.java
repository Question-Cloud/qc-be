package com.eager.questioncloud.core.domain.question.implement;

import com.eager.questioncloud.core.domain.library.repository.LibraryRepository;
import com.eager.questioncloud.core.domain.question.common.QuestionFilter;
import com.eager.questioncloud.core.domain.question.dto.QuestionDto.QuestionInformation;
import com.eager.questioncloud.core.domain.question.model.Question;
import com.eager.questioncloud.core.domain.question.repository.QuestionRepository;
import com.eager.questioncloud.core.domain.review.model.QuestionReviewStatistics;
import com.eager.questioncloud.core.domain.review.repository.QuestionReviewStatisticsRepository;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionHubReader {
    private final QuestionRepository questionRepository;
    private final LibraryRepository libraryRepository;
    private final QuestionReviewStatisticsRepository questionReviewStatisticsRepository;

    public List<QuestionInformation> getQuestions(QuestionFilter questionFilter) {
        List<Question> questions = questionRepository.getQuestionsByFilter(questionFilter);

        List<Long> questionIds = questions.stream()
            .map(Question::getId)
            .toList();

        Map<Long, QuestionReviewStatistics> reviewStatistics = questionReviewStatisticsRepository.findByQuestionIdIn(questionIds);

        Set<Long> alreadyOwnedQuestionIds = libraryRepository.checkIsOwned(
            questionFilter.getUserId(),
            questionIds
        );

        return QuestionInformation.of(questions, alreadyOwnedQuestionIds, reviewStatistics);
    }

    public QuestionInformation getQuestion(Long questionId, Long userId) {
        Question question = questionRepository.get(questionId);
        QuestionReviewStatistics reviewStatistics = questionReviewStatisticsRepository.get(questionId);
        Boolean isOwned = libraryRepository.isOwned(userId, questionId);

        return QuestionInformation.forHubDetail(question, isOwned, reviewStatistics);
    }

    public int countByQuestionFilter(QuestionFilter questionFilter) {
        return questionRepository.getTotalFiltering(questionFilter);
    }
}
