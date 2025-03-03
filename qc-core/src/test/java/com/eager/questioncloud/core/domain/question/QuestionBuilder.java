package com.eager.questioncloud.core.domain.question;

import com.eager.questioncloud.core.domain.question.enums.QuestionLevel;
import com.eager.questioncloud.core.domain.question.enums.QuestionStatus;
import com.eager.questioncloud.core.domain.question.enums.QuestionType;
import com.eager.questioncloud.core.domain.question.enums.Subject;
import com.eager.questioncloud.core.domain.question.model.Question;
import com.eager.questioncloud.core.domain.question.model.QuestionContent;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public class QuestionBuilder {
    private Long id;
    @Builder.Default
    private Long creatorId = 1L;
    @Builder.Default
    private QuestionContent questionContent = new QuestionContent(
        1L,
        Subject.Biology,
        "questionTitle",
        "questionDescription",
        "questionThumbnail",
        "questionFileUrl",
        "questionExplanationUrl",
        QuestionType.Past,
        QuestionLevel.LEVEL4,
        1000
    );
    @Builder.Default
    private QuestionStatus questionStatus = QuestionStatus.Available;
    @Builder.Default
    private int count = 100;
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public Question toQuestion() {
        return new Question(id, creatorId, questionContent, questionStatus, count, createdAt);
    }
}
