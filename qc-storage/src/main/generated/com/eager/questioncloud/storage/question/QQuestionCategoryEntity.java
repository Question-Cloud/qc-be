package com.eager.questioncloud.storage.question;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QQuestionCategoryEntity is a Querydsl query type for QuestionCategoryEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuestionCategoryEntity extends EntityPathBase<QuestionCategoryEntity> {

    private static final long serialVersionUID = 1699056570L;

    public static final QQuestionCategoryEntity questionCategoryEntity = new QQuestionCategoryEntity("questionCategoryEntity");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isParent = createBoolean("isParent");

    public final NumberPath<Long> parentId = createNumber("parentId", Long.class);

    public final EnumPath<com.eager.questioncloud.core.domain.question.vo.Subject> subject = createEnum("subject", com.eager.questioncloud.core.domain.question.vo.Subject.class);

    public final StringPath title = createString("title");

    public QQuestionCategoryEntity(String variable) {
        super(QuestionCategoryEntity.class, forVariable(variable));
    }

    public QQuestionCategoryEntity(Path<? extends QuestionCategoryEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QQuestionCategoryEntity(PathMetadata metadata) {
        super(QuestionCategoryEntity.class, metadata);
    }

}

