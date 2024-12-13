package com.eager.questioncloud.core.domain.payment.infrastructure;

import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
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
    private String orderId;

    @Column
    private Long questionId;

    @Column
    private int price;

    @Builder
    public QuestionPaymentOrderEntity(Long id, String orderId, Long questionId, int price) {
        this.id = id;
        this.orderId = orderId;
        this.questionId = questionId;
        this.price = price;
    }

    public static List<QuestionPaymentOrderEntity> from(QuestionPaymentOrder questionPaymentOrder) {
        return questionPaymentOrder.getItems()
            .stream()
            .map(item -> QuestionPaymentOrderEntity
                .builder()
                .id(item.getId())
                .orderId(questionPaymentOrder.getOrderId())
                .questionId(item.getQuestionId())
                .price(item.getPrice())
                .build()
            )
            .toList();
    }
}
