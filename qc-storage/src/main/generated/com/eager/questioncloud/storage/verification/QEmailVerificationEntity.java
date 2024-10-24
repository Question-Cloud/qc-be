package com.eager.questioncloud.storage.verification;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QEmailVerificationEntity is a Querydsl query type for EmailVerificationEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEmailVerificationEntity extends EntityPathBase<EmailVerificationEntity> {

    private static final long serialVersionUID = -1626057220L;

    public static final QEmailVerificationEntity emailVerificationEntity = new QEmailVerificationEntity("emailVerificationEntity");

    public final EnumPath<com.eager.questioncloud.core.domain.verification.vo.EmailVerificationType> emailVerificationType = createEnum("emailVerificationType", com.eager.questioncloud.core.domain.verification.vo.EmailVerificationType.class);

    public final BooleanPath isVerified = createBoolean("isVerified");

    public final StringPath resendToken = createString("resendToken");

    public final StringPath token = createString("token");

    public final NumberPath<Long> uid = createNumber("uid", Long.class);

    public QEmailVerificationEntity(String variable) {
        super(EmailVerificationEntity.class, forVariable(variable));
    }

    public QEmailVerificationEntity(Path<? extends EmailVerificationEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEmailVerificationEntity(PathMetadata metadata) {
        super(EmailVerificationEntity.class, metadata);
    }

}

