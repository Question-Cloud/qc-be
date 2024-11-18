package com.eager.questioncloud.domain.cart;

import com.eager.questioncloud.domain.question.vo.Subject;
import lombok.Getter;

@Getter
public class CartItemDetail {
    private Long id;
    private Long questionId;
    private String title;
    private String thumbnail;
    private String creatorName;
    private Subject subject;
    private int price;

    public CartItemDetail(Long id, Long questionId, String title, String thumbnail, String creatorName, Subject subject, int price) {
        this.id = id;
        this.questionId = questionId;
        this.title = title;
        this.thumbnail = thumbnail;
        this.creatorName = creatorName;
        this.subject = subject;
        this.price = price;
    }
}
