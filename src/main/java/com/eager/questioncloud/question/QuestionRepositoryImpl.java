package com.eager.questioncloud.question;

import static com.eager.questioncloud.question.QQuestionCategoryEntity.questionCategoryEntity;
import static com.eager.questioncloud.question.QQuestionEntity.questionEntity;
import static com.eager.questioncloud.user.QUserEntity.userEntity;

import com.eager.questioncloud.question.QuestionDto.QuestionFilterItem;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuestionRepositoryImpl implements QuestionRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public int getTotalFiltering(List<Long> questionCategoryIds, List<QuestionLevel> questionLevels) {
        Integer total = jpaQueryFactory.select(questionEntity.id.count().intValue())
            .from(questionEntity)
            .innerJoin(userEntity).on(userEntity.uid.eq(questionEntity.creatorId))
            .innerJoin(questionCategoryEntity).on(questionCategoryEntity.id.eq(questionEntity.questionCategoryId))
            .where(questionEntity.questionLevel.in(questionLevels), questionCategoryEntity.id.in(questionCategoryIds))
            .fetchFirst();

        if (total == null) {
            return 0;
        }

        return total;
    }

    @Override
    public List<QuestionFilterItem> getQuestionListByFiltering(List<Long> questionCategoryIds, List<QuestionLevel> questionLevels,
        QuestionSortType sort) {
        return jpaQueryFactory.select(
                Projections.constructor(
                    QuestionFilterItem.class,
                    questionEntity.id,
                    questionEntity.title,
                    questionEntity.thumbnail,
                    userEntity.name,
                    questionEntity.questionLevel,
                    questionEntity.price))
            .from(questionEntity)
            .innerJoin(userEntity).on(userEntity.uid.eq(questionEntity.creatorId))
            .innerJoin(questionCategoryEntity).on(questionCategoryEntity.id.eq(questionEntity.questionCategoryId))
            .orderBy(sort(sort), questionEntity.id.desc())
            .where(questionEntity.questionLevel.in(questionLevels), questionCategoryEntity.id.in(questionCategoryIds))
            .fetch();
    }

    private OrderSpecifier<?> sort(QuestionSortType sort) {
        switch (sort) {
            case Popularity -> {
                return questionEntity.count.desc();
            }
            case Rate -> {
                return null;
            }
            case Latest -> {
                return questionEntity.createdAt.desc();
            }
            case LEVEL -> {
                return questionEntity.questionLevel.desc();
            }
            default -> {
                return null;
            }
        }
    }
}
