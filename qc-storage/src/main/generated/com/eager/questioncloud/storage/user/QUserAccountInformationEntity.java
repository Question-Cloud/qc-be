package com.eager.questioncloud.storage.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserAccountInformationEntity is a Querydsl query type for UserAccountInformationEntity
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QUserAccountInformationEntity extends BeanPath<UserAccountInformationEntity> {

    private static final long serialVersionUID = -1979989605L;

    public static final QUserAccountInformationEntity userAccountInformationEntity = new QUserAccountInformationEntity("userAccountInformationEntity");

    public final EnumPath<com.eager.questioncloud.core.domain.user.vo.AccountType> accountType = createEnum("accountType", com.eager.questioncloud.core.domain.user.vo.AccountType.class);

    public final StringPath password = createString("password");

    public final StringPath socialUid = createString("socialUid");

    public QUserAccountInformationEntity(String variable) {
        super(UserAccountInformationEntity.class, forVariable(variable));
    }

    public QUserAccountInformationEntity(Path<? extends UserAccountInformationEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserAccountInformationEntity(PathMetadata metadata) {
        super(UserAccountInformationEntity.class, metadata);
    }

}

