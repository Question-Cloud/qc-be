package com.eager.questioncloud.storage.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserInformationEntity is a Querydsl query type for UserInformationEntity
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QUserInformationEntity extends BeanPath<UserInformationEntity> {

    private static final long serialVersionUID = -1343465930L;

    public static final QUserInformationEntity userInformationEntity = new QUserInformationEntity("userInformationEntity");

    public final StringPath email = createString("email");

    public final StringPath name = createString("name");

    public final StringPath phone = createString("phone");

    public final StringPath profileImage = createString("profileImage");

    public QUserInformationEntity(String variable) {
        super(UserInformationEntity.class, forVariable(variable));
    }

    public QUserInformationEntity(Path<? extends UserInformationEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserInformationEntity(PathMetadata metadata) {
        super(UserInformationEntity.class, metadata);
    }

}

