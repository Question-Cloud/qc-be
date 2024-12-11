package com.eager.questioncloud.core.domain.question.dto;

import com.eager.questioncloud.core.domain.question.enums.QuestionLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class QuestionInformation {
    private Long id;
    private String title;
    private String parentCategory;
    private String childCategory;
    private String thumbnail;
    private String creatorName;
    private QuestionLevel questionLevel;
    private int price;
    private Double rate;
    private Boolean isOwned;
}
