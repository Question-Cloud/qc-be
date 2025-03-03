package com.eager.questioncloud.core.domain.question.infrastructure.repository

import com.eager.questioncloud.core.domain.question.dto.QuestionCategoryGroupBySubject.MainQuestionCategory
import com.eager.questioncloud.core.domain.question.dto.QuestionCategoryGroupBySubject.SubQuestionCategory
import com.eager.questioncloud.core.domain.question.infrastructure.entity.QQuestionCategoryEntity
import com.querydsl.core.group.GroupBy
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class QuestionCategoryRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : QuestionCategoryRepository {
    val parent = QQuestionCategoryEntity("parent")
    val child = QQuestionCategoryEntity("child")

    override fun getMainQuestionCategories(): List<MainQuestionCategory> {
        return jpaQueryFactory.select(parent, child)
            .from(parent)
            .leftJoin(child).on(child.parentId.eq(parent.id))
            .where(parent.isParent.isTrue())
            .transform(
                GroupBy.groupBy(parent.id)
                    .list(
                        Projections.constructor(
                            MainQuestionCategory::class.java,
                            parent.title,
                            parent.subject,
                            GroupBy.list(
                                Projections.constructor(
                                    SubQuestionCategory::class.java,
                                    child.id,
                                    child.title
                                )
                            )
                        )
                    )
            )
    }
}
