package com.eager.questioncloud.storage.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCreateSocialUserInformationEntity is a Querydsl query type for CreateSocialUserInformationEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCreateSocialUserInformationEntity extends EntityPathBase<CreateSocialUserInformationEntity> {

    private static final long serialVersionUID = -454122707L;

    public static final QCreateSocialUserInformationEntity createSocialUserInformationEntity = new QCreateSocialUserInformationEntity("createSocialUserInformationEntity");

    public final EnumPath<com.eager.questioncloud.core.domain.user.vo.AccountType> accountType = createEnum("accountType", com.eager.questioncloud.core.domain.user.vo.AccountType.class);

    public final BooleanPath isUsed = createBoolean("isUsed");

    public final StringPath registerToken = createString("registerToken");

    public final StringPath socialUid = createString("socialUid");

    public QCreateSocialUserInformationEntity(String variable) {
        super(CreateSocialUserInformationEntity.class, forVariable(variable));
    }

    public QCreateSocialUserInformationEntity(Path<? extends CreateSocialUserInformationEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCreateSocialUserInformationEntity(PathMetadata metadata) {
        super(CreateSocialUserInformationEntity.class, metadata);
    }

}

