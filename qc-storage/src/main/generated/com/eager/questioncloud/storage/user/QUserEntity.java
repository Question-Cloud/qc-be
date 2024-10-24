package com.eager.questioncloud.storage.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserEntity is a Querydsl query type for UserEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserEntity extends EntityPathBase<UserEntity> {

    private static final long serialVersionUID = -35504068L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserEntity userEntity = new QUserEntity("userEntity");

    public final NumberPath<Long> uid = createNumber("uid", Long.class);

    public final QUserAccountInformationEntity userAccountInformationEntity;

    public final QUserInformationEntity userInformationEntity;

    public final EnumPath<com.eager.questioncloud.core.domain.user.vo.UserStatus> userStatus = createEnum("userStatus", com.eager.questioncloud.core.domain.user.vo.UserStatus.class);

    public final EnumPath<com.eager.questioncloud.core.domain.user.vo.UserType> userType = createEnum("userType", com.eager.questioncloud.core.domain.user.vo.UserType.class);

    public QUserEntity(String variable) {
        this(UserEntity.class, forVariable(variable), INITS);
    }

    public QUserEntity(Path<? extends UserEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserEntity(PathMetadata metadata, PathInits inits) {
        this(UserEntity.class, metadata, inits);
    }

    public QUserEntity(Class<? extends UserEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.userAccountInformationEntity = inits.isInitialized("userAccountInformationEntity") ? new QUserAccountInformationEntity(forProperty("userAccountInformationEntity")) : null;
        this.userInformationEntity = inits.isInitialized("userInformationEntity") ? new QUserInformationEntity(forProperty("userInformationEntity")) : null;
    }

}

