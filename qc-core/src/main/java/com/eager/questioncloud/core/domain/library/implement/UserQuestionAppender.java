package com.eager.questioncloud.core.domain.library.implement;

import com.eager.questioncloud.core.domain.library.model.UserQuestion;
import com.eager.questioncloud.core.domain.library.repository.LibraryRepository;
import com.eager.questioncloud.core.domain.question.model.Question;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserQuestionAppender {
    private final LibraryRepository libraryRepository;

    public void appendUserQuestions(Long userId, List<Question> questions) {
        libraryRepository.saveAll(UserQuestion.create(userId, questions));
    }
}
