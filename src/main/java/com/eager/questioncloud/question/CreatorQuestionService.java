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
    private final CreatorQuestionReader creatorQuestionReader;

    public int count(Long userId) {
        return creatorQuestionReader.count(userId);
    }

    public List<QuestionInformation> getQuestions(Long userId, Pageable pageable) {
        return creatorQuestionReader.getQuestions(userId, pageable);
    }

    public QuestionContent getQuestionContent(Long userId, Long questionId) {
        return creatorQuestionReader.getQuestionContent(userId, questionId);
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
