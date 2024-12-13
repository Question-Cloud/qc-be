package com.eager.questioncloud.application;

import com.eager.questioncloud.application.api.workspace.service.CreatorQuestionService;
import com.eager.questioncloud.core.domain.question.enums.QuestionLevel;
import com.eager.questioncloud.core.domain.question.enums.QuestionType;
import com.eager.questioncloud.core.domain.question.enums.Subject;
import com.eager.questioncloud.core.domain.question.model.QuestionContent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
public class QuestionDummyBatch {
    @Autowired
    private CreatorQuestionService creatorQuestionService;

    @Test
    void addDummyQuestion() {
        for (int i = 0; i < 1000; i++) {
            creatorQuestionService.registerQuestion(1L,
                QuestionContent.builder()
                    .questionCategoryId(25L)
                    .subject(Subject.Biology)
                    .title("question-" + i)
                    .description("description-" + i)
                    .thumbnail("thumbnail-" + i)
                    .fileUrl("file-url-" + i)
                    .explanationUrl("explanation-url-" + i)
                    .questionType(QuestionType.Past)
                    .questionLevel(QuestionLevel.LEVEL4)
                    .price(1000)
                    .build()
            );
        }
    }
}
