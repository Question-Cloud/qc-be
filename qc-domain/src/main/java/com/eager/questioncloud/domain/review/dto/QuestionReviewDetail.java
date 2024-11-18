package com.eager.questioncloud.domain.review.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestionReviewDetail {
    private Long id;
    private String name;
    private Boolean isCreator;
    private Boolean isWriter;
    private Integer reviewCount;
    private Double rateAverage;
    private Integer rate;
    private String comment;
    private LocalDateTime createdAt;
}
