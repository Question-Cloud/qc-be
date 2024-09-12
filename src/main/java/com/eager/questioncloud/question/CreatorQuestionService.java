package com.eager.questioncloud.question;

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
    private final CreatorQuestionRegister creatorQuestionRegister;
    private final CreatorQuestionUpdater creatorQuestionUpdater;
    private final CreatorQuestionRemover creatorQuestionRemover;
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
        return creatorQuestionRegister.register(userId, questionContent);
    }

    public void modify(Long userId, Long questionId, QuestionContent questionContent) {
        creatorQuestionUpdater.modify(userId, questionId, questionContent);
    }

    public void delete(Long userId, Long questionId) {
        creatorQuestionRemover.remove(userId, questionId);
    }
}
