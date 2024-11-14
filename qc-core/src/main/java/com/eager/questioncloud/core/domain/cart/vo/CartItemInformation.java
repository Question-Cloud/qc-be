package com.eager.questioncloud.core.domain.cart.vo;

import com.eager.questioncloud.core.domain.question.model.Question;
import com.eager.questioncloud.core.domain.question.vo.Subject;
import lombok.Getter;

@Getter
public class CartItemInformation {
    private Long questionId;
    private String title;
    private String thumbnail;
    private String creatorName;
    private Subject subject;
    private int price;

    public CartItemInformation(Long questionId, String title, String thumbnail, String creatorName, Subject subject, int price) {
        this.questionId = questionId;
        this.title = title;
        this.thumbnail = thumbnail;
        this.creatorName = creatorName;
        this.subject = subject;
        this.price = price;
    }

    //TODO Question 도메인 리팩터링 후 creatorName 받아 오기
    public static CartItemInformation create(Question question) {
        return new CartItemInformation(
            question.getId(),
            question.getQuestionContent().getTitle(),
            question.getQuestionContent().getThumbnail(),
            "creatorName",
            question.getQuestionContent().getSubject(),
            question.getQuestionContent().getPrice());
    }
}
