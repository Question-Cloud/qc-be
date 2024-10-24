package com.eager.questioncloud.core.domain.question.vo;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QQuestionContent is a Querydsl query type for QuestionContent
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QQuestionContent extends BeanPath<QuestionContent> {

    private static final long serialVersionUID = 403510761L;

    public static final QQuestionContent questionContent = new QQuestionContent("questionContent");

    public final StringPath description = createString("description");

    public final StringPath explanationUrl = createString("explanationUrl");

    public final StringPath fileUrl = createString("fileUrl");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final NumberPath<Long> questionCategoryId = createNumber("questionCategoryId", Long.class);

    public final EnumPath<QuestionLevel> questionLevel = createEnum("questionLevel", QuestionLevel.class);

    public final EnumPath<QuestionType> questionType = createEnum("questionType", QuestionType.class);

    public final EnumPath<Subject> subject = createEnum("subject", Subject.class);

    public final StringPath thumbnail = createString("thumbnail");

    public final StringPath title = createString("title");

    public QQuestionContent(String variable) {
        super(QuestionContent.class, forVariable(variable));
    }

    public QQuestionContent(Path<? extends QuestionContent> path) {
        super(path.getType(), path.getMetadata());
    }

    public QQuestionContent(PathMetadata metadata) {
        super(QuestionContent.class, metadata);
    }

}

