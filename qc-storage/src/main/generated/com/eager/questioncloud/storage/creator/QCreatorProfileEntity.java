package com.eager.questioncloud.storage.creator;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCreatorProfileEntity is a Querydsl query type for CreatorProfileEntity
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QCreatorProfileEntity extends BeanPath<CreatorProfileEntity> {

    private static final long serialVersionUID = 543864343L;

    public static final QCreatorProfileEntity creatorProfileEntity = new QCreatorProfileEntity("creatorProfileEntity");

    public final StringPath introduction = createString("introduction");

    public final EnumPath<com.eager.questioncloud.core.domain.question.vo.Subject> mainSubject = createEnum("mainSubject", com.eager.questioncloud.core.domain.question.vo.Subject.class);

    public QCreatorProfileEntity(String variable) {
        super(CreatorProfileEntity.class, forVariable(variable));
    }

    public QCreatorProfileEntity(Path<? extends CreatorProfileEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCreatorProfileEntity(PathMetadata metadata) {
        super(CreatorProfileEntity.class, metadata);
    }

}

