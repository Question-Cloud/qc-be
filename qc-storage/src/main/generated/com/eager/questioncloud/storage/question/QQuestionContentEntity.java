package com.eager.questioncloud.storage.question;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QQuestionContentEntity is a Querydsl query type for QuestionContentEntity
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QQuestionContentEntity extends BeanPath<QuestionContentEntity> {

    private static final long serialVersionUID = 1639892323L;

    public static final QQuestionContentEntity questionContentEntity = new QQuestionContentEntity("questionContentEntity");

    public final StringPath description = createString("description");

    public final StringPath explanationUrl = createString("explanationUrl");

    public final StringPath fileUrl = createString("fileUrl");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final NumberPath<Long> questionCategoryId = createNumber("questionCategoryId", Long.class);

    public final EnumPath<com.eager.questioncloud.core.domain.question.vo.QuestionLevel> questionLevel = createEnum("questionLevel", com.eager.questioncloud.core.domain.question.vo.QuestionLevel.class);

    public final EnumPath<com.eager.questioncloud.core.domain.question.vo.QuestionType> questionType = createEnum("questionType", com.eager.questioncloud.core.domain.question.vo.QuestionType.class);

    public final EnumPath<com.eager.questioncloud.core.domain.question.vo.Subject> subject = createEnum("subject", com.eager.questioncloud.core.domain.question.vo.Subject.class);

    public final StringPath thumbnail = createString("thumbnail");

    public final StringPath title = createString("title");

    public QQuestionContentEntity(String variable) {
        super(QuestionContentEntity.class, forVariable(variable));
    }

    public QQuestionContentEntity(Path<? extends QuestionContentEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QQuestionContentEntity(PathMetadata metadata) {
        super(QuestionContentEntity.class, metadata);
    }

}

