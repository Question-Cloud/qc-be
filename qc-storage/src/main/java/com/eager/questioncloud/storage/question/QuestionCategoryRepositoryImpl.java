package com.eager.questioncloud.storage.question;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.eager.questioncloud.core.domain.question.dto.QuestionCategoryDto.QuestionCategoryItem;
import com.eager.questioncloud.core.domain.question.dto.QuestionCategoryDto.SubQuestionCategoryItem;
import com.eager.questioncloud.core.domain.question.repository.QuestionCategoryRepository;
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
    public List<QuestionCategoryItem> getQuestionCategories() {
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
                            QuestionCategoryItem.class,
                            parent.title,
                            parent.subject,
                            list(
                                Projections.constructor(SubQuestionCategoryItem.class,
                                    child.id,
                                    child.title))
                        )
                    )
            );
    }
}
