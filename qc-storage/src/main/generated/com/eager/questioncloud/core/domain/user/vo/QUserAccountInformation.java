package com.eager.questioncloud.core.domain.user.vo;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserAccountInformation is a Querydsl query type for UserAccountInformation
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QUserAccountInformation extends BeanPath<UserAccountInformation> {

    private static final long serialVersionUID = -791038695L;

    public static final QUserAccountInformation userAccountInformation = new QUserAccountInformation("userAccountInformation");

    public final EnumPath<AccountType> accountType = createEnum("accountType", AccountType.class);

    public final StringPath password = createString("password");

    public final StringPath socialUid = createString("socialUid");

    public QUserAccountInformation(String variable) {
        super(UserAccountInformation.class, forVariable(variable));
    }

    public QUserAccountInformation(Path<? extends UserAccountInformation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserAccountInformation(PathMetadata metadata) {
        super(UserAccountInformation.class, metadata);
    }

}

