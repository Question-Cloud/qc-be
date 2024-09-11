package com.eager.questioncloud.question;

import com.eager.questioncloud.creator.Creator;
import com.eager.questioncloud.creator.CreatorReader;
import com.eager.questioncloud.question.QuestionDto.QuestionInformation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatorQuestionService {
    private final CreatorReader creatorReader;
    private final QuestionRegister questionRegister;
    private final QuestionUpdater questionUpdater;
    private final QuestionRemover questionRemover;
    private final QuestionReader questionReader;

    public int count(Long userId) {
        Creator creator = creatorReader.getByUserId(userId);
        return questionReader.getTotalFiltering(
            new QuestionFilter(userId, null, null, null, creator.getId(), QuestionSortType.Latest, null)
        );
    }

    public List<QuestionInformation> getQuestions(Long userId, Pageable pageable) {
        Creator creator = creatorReader.getByUserId(userId);
        return questionReader.getQuestionListByFiltering(
            new QuestionFilter(userId, null, null, null, creator.getId(), QuestionSortType.Latest, pageable)
        );
    }

    public QuestionContent get(Long userId, Long questionId) {
        Creator creator = creatorReader.getByUserId(userId);
        return questionReader.getQuestionContent(creator.getId(), questionId);
    }

    public Question register(Long userId, QuestionContent questionContent) {
        Creator creator = creatorReader.getByUserId(userId);
        return questionRegister.register(creator.getId(), questionContent);
    }

    public void modify(Long userId, Long questionId, QuestionContent questionContent) {
        Creator creator = creatorReader.getByUserId(userId);
        questionUpdater.modify(creator.getId(), questionId, questionContent);
    }

    public void delete(Long userId, Long questionId) {
        Creator creator = creatorReader.getByUserId(userId);
        questionRemover.remove(creator.getId(), questionId);
    }
}
