package com.eager.questioncloud.core.domain.user.vo;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserInformation is a Querydsl query type for UserInformation
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QUserInformation extends BeanPath<UserInformation> {

    private static final long serialVersionUID = 2014803474L;

    public static final QUserInformation userInformation = new QUserInformation("userInformation");

    public final StringPath email = createString("email");

    public final StringPath name = createString("name");

    public final StringPath phone = createString("phone");

    public final StringPath profileImage = createString("profileImage");

    public QUserInformation(String variable) {
        super(UserInformation.class, forVariable(variable));
    }

    public QUserInformation(Path<? extends UserInformation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserInformation(PathMetadata metadata) {
        super(UserInformation.class, metadata);
    }

}

