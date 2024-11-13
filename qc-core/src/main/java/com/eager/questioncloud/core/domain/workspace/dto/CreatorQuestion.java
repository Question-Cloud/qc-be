package com.eager.questioncloud.core.domain.workspace.dto;

import com.eager.questioncloud.core.domain.question.model.Question;
import com.eager.questioncloud.core.domain.question.vo.QuestionLevel;
import com.eager.questioncloud.core.domain.question.vo.Subject;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreatorQuestion {
    private Long id;
    private String title;
    private Subject subject;
    private String parentCategory;
    private String childCategory;
    private String thumbnail;
    private QuestionLevel questionLevel;
    private int price;

    public static List<CreatorQuestion> from(List<Question> questions) {
        return questions.stream()
            .map(question ->
                CreatorQuestion.builder()
                    .id(question.getId())
                    .title(question.getQuestionContent().getTitle())
                    .subject(question.getCategory().getChildCategory().getSubject())
                    .parentCategory(question.getCategory().getParentCategory().getTitle())
                    .childCategory(question.getCategory().getChildCategory().getTitle())
                    .thumbnail(question.getQuestionContent().getThumbnail())
                    .questionLevel(question.getQuestionContent().getQuestionLevel())
                    .price(question.getQuestionContent().getPrice())
                    .build())
            .collect(Collectors.toList());
    }
}
