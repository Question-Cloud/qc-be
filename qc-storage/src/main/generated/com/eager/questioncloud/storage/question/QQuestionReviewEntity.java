package com.eager.questioncloud.storage.question;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QQuestionReviewEntity is a Querydsl query type for QuestionReviewEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuestionReviewEntity extends EntityPathBase<QuestionReviewEntity> {

    private static final long serialVersionUID = -633531180L;

    public static final QQuestionReviewEntity questionReviewEntity = new QQuestionReviewEntity("questionReviewEntity");

    public final StringPath comment = createString("comment");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final NumberPath<Long> questionId = createNumber("questionId", Long.class);

    public final NumberPath<Integer> rate = createNumber("rate", Integer.class);

    public final NumberPath<Long> reviewerId = createNumber("reviewerId", Long.class);

    public QQuestionReviewEntity(String variable) {
        super(QuestionReviewEntity.class, forVariable(variable));
    }

    public QQuestionReviewEntity(Path<? extends QuestionReviewEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QQuestionReviewEntity(PathMetadata metadata) {
        super(QuestionReviewEntity.class, metadata);
    }

}

