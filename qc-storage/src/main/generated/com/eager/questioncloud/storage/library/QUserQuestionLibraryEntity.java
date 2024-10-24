package com.eager.questioncloud.storage.library;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserQuestionLibraryEntity is a Querydsl query type for UserQuestionLibraryEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserQuestionLibraryEntity extends EntityPathBase<UserQuestionLibraryEntity> {

    private static final long serialVersionUID = -1439813275L;

    public static final QUserQuestionLibraryEntity userQuestionLibraryEntity = new QUserQuestionLibraryEntity("userQuestionLibraryEntity");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isUsed = createBoolean("isUsed");

    public final NumberPath<Long> questionId = createNumber("questionId", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QUserQuestionLibraryEntity(String variable) {
        super(UserQuestionLibraryEntity.class, forVariable(variable));
    }

    public QUserQuestionLibraryEntity(Path<? extends UserQuestionLibraryEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserQuestionLibraryEntity(PathMetadata metadata) {
        super(UserQuestionLibraryEntity.class, metadata);
    }

}

