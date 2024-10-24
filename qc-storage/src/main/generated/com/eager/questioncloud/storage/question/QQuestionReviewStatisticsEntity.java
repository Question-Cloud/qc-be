package com.eager.questioncloud.storage.question;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QQuestionReviewStatisticsEntity is a Querydsl query type for QuestionReviewStatisticsEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuestionReviewStatisticsEntity extends EntityPathBase<QuestionReviewStatisticsEntity> {

    private static final long serialVersionUID = 629166743L;

    public static final QQuestionReviewStatisticsEntity questionReviewStatisticsEntity = new QQuestionReviewStatisticsEntity("questionReviewStatisticsEntity");

    public final NumberPath<Double> averageRate = createNumber("averageRate", Double.class);

    public final NumberPath<Long> questionId = createNumber("questionId", Long.class);

    public final NumberPath<Integer> reviewCount = createNumber("reviewCount", Integer.class);

    public final NumberPath<Integer> totalRate = createNumber("totalRate", Integer.class);

    public QQuestionReviewStatisticsEntity(String variable) {
        super(QuestionReviewStatisticsEntity.class, forVariable(variable));
    }

    public QQuestionReviewStatisticsEntity(Path<? extends QuestionReviewStatisticsEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QQuestionReviewStatisticsEntity(PathMetadata metadata) {
        super(QuestionReviewStatisticsEntity.class, metadata);
    }

}

