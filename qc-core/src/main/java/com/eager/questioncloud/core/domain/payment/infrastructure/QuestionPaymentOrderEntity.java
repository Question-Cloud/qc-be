package com.eager.questioncloud.core.domain.payment.infrastructure;

import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "question_payment_order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionPaymentOrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String paymentId;

    @Column
    private Long questionId;

    @Column
    private int price;

    @Builder
    public QuestionPaymentOrderEntity(Long id, String paymentId, Long questionId, int price) {
        this.id = id;
        this.paymentId = paymentId;
        this.questionId = questionId;
        this.price = price;
    }

    public static List<QuestionPaymentOrder> toModel(List<QuestionPaymentOrderEntity> orders) {
        return orders
            .stream()
            .map(QuestionPaymentOrderEntity::toModel)
            .collect(Collectors.toList());
    }

    public QuestionPaymentOrder toModel() {
        return QuestionPaymentOrder.builder()
            .id(id)
            .paymentId(paymentId)
            .questionId(questionId)
            .price(price)
            .build();
    }

    public static QuestionPaymentOrderEntity from(QuestionPaymentOrder questionPaymentOrder) {
        return QuestionPaymentOrderEntity.builder()
            .id(questionPaymentOrder.getId())
            .paymentId(questionPaymentOrder.getPaymentId())
            .questionId(questionPaymentOrder.getQuestionId())
            .price(questionPaymentOrder.getPrice())
            .build();
    }

    public static List<QuestionPaymentOrderEntity> from(List<QuestionPaymentOrder> questionPaymentOrders) {
        return questionPaymentOrders
            .stream()
            .map(QuestionPaymentOrderEntity::from)
            .collect(Collectors.toList());
    }
}
