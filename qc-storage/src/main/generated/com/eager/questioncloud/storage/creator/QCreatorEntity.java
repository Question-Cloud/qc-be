package com.eager.questioncloud.storage.creator;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCreatorEntity is a Querydsl query type for CreatorEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCreatorEntity extends EntityPathBase<CreatorEntity> {

    private static final long serialVersionUID = 1405538072L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCreatorEntity creatorEntity = new QCreatorEntity("creatorEntity");

    public final com.eager.questioncloud.core.domain.creator.vo.QCreatorProfile creatorProfile;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QCreatorEntity(String variable) {
        this(CreatorEntity.class, forVariable(variable), INITS);
    }

    public QCreatorEntity(Path<? extends CreatorEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCreatorEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCreatorEntity(PathMetadata metadata, PathInits inits) {
        this(CreatorEntity.class, metadata, inits);
    }

    public QCreatorEntity(Class<? extends CreatorEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.creatorProfile = inits.isInitialized("creatorProfile") ? new com.eager.questioncloud.core.domain.creator.vo.QCreatorProfile(forProperty("creatorProfile")) : null;
    }

}

