package com.eager.questioncloud.core.domain.creator.vo;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCreatorProfile is a Querydsl query type for CreatorProfile
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QCreatorProfile extends BeanPath<CreatorProfile> {

    private static final long serialVersionUID = -941889069L;

    public static final QCreatorProfile creatorProfile = new QCreatorProfile("creatorProfile");

    public final StringPath introduction = createString("introduction");

    public final SimplePath<javax.security.auth.Subject> mainSubject = createSimple("mainSubject", javax.security.auth.Subject.class);

    public QCreatorProfile(String variable) {
        super(CreatorProfile.class, forVariable(variable));
    }

    public QCreatorProfile(Path<? extends CreatorProfile> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCreatorProfile(PathMetadata metadata) {
        super(CreatorProfile.class, metadata);
    }

}

