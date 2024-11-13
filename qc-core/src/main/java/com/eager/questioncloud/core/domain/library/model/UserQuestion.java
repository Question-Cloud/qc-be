package com.eager.questioncloud.core.domain.library.model;

import com.eager.questioncloud.core.domain.question.model.Question;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserQuestion {
    private Long id;
    private Long userId;
    private Question question;
    private Boolean isUsed;
    private LocalDateTime createdAt;

    @Builder
    public UserQuestion(Long id, Long userId, Question question, Boolean isUsed, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.question = question;
        this.isUsed = isUsed;
        this.createdAt = createdAt;
    }

    public static List<UserQuestion> create(Long userId, List<Question> questions) {
        LocalDateTime now = LocalDateTime.now();
        return questions
            .stream()
            .map(question -> UserQuestion
                .builder()
                .userId(userId)
                .question(question)
                .isUsed(false)
                .createdAt(now)
                .build())
            .collect(Collectors.toList());
    }
}
