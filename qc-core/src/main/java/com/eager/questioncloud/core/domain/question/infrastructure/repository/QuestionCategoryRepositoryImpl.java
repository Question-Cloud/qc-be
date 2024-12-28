package com.eager.questioncloud.core.domain.question.infrastructure.repository;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.eager.questioncloud.core.domain.question.dto.QuestionCategoryGroupBySubject.MainQuestionCategory;
import com.eager.questioncloud.core.domain.question.dto.QuestionCategoryGroupBySubject.SubQuestionCategory;
import com.eager.questioncloud.core.domain.question.infrastructure.entity.QQuestionCategoryEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuestionCategoryRepositoryImpl implements QuestionCategoryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<MainQuestionCategory> getMainQuestionCategories() {
        QQuestionCategoryEntity parent = new QQuestionCategoryEntity("parent");
        QQuestionCategoryEntity child = new QQuestionCategoryEntity("child");
        return jpaQueryFactory.select(parent, child)
            .from(parent)
            .leftJoin(child).on(child.parentId.eq(parent.id))
            .where(parent.isParent.isTrue())
            .transform(
                groupBy(parent.id)
                    .list(
                        Projections.constructor(
                            MainQuestionCategory.class,
                            parent.title,
                            parent.subject,
                            list(
                                Projections.constructor(SubQuestionCategory.class,
                                    child.id,
                                    child.title))
                        )
                    )
            );
    }
}
