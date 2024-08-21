package com.eager.questioncloud.question;

import static com.eager.questioncloud.question.QQuestionCategoryEntity.questionCategoryEntity;
import static com.eager.questioncloud.question.QQuestionEntity.questionEntity;
import static com.eager.questioncloud.user.QUserEntity.userEntity;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.question.QuestionDto.QuestionDetail;
import com.eager.questioncloud.question.QuestionDto.QuestionFilterItem;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuestionRepositoryImpl implements QuestionRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public int getTotalFiltering(List<Long> questionCategoryIds, List<QuestionLevel> questionLevels, QuestionType questionType) {
        Integer total = jpaQueryFactory.select(questionEntity.id.count().intValue())
            .from(questionEntity)
            .innerJoin(userEntity).on(userEntity.uid.eq(questionEntity.creatorId))
            .innerJoin(questionCategoryEntity).on(questionCategoryEntity.id.eq(questionEntity.questionCategoryId))
            .where(
                questionEntity.questionLevel.in(questionLevels),
                questionCategoryEntity.id.in(questionCategoryIds),
                questionEntity.questionType.eq(questionType))
            .fetchFirst();

        if (total == null) {
            return 0;
        }

        return total;
    }

    @Override
    public List<QuestionFilterItem> getQuestionListByFiltering(List<Long> questionCategoryIds, List<QuestionLevel> questionLevels,
        QuestionType questionType, QuestionSortType sort, Pageable pageable) {
        QQuestionCategoryEntity parent = new QQuestionCategoryEntity("parent");
        QQuestionCategoryEntity child = new QQuestionCategoryEntity("child");
        return jpaQueryFactory.select(
                Projections.constructor(
                    QuestionFilterItem.class,
                    questionEntity.id,
                    questionEntity.title,
                    parent.title,
                    child.title,
                    questionEntity.thumbnail,
                    userEntity.name,
                    questionEntity.questionLevel,
                    questionEntity.price))
            .from(questionEntity)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .innerJoin(child).on(child.id.eq(questionEntity.questionCategoryId))
            .innerJoin(parent).on(parent.id.eq(child.parentId))
            .innerJoin(userEntity).on(userEntity.uid.eq(questionEntity.creatorId))
            .innerJoin(questionCategoryEntity).on(questionCategoryEntity.id.eq(questionEntity.questionCategoryId))
            .orderBy(sort(sort), questionEntity.id.desc())
            .where(
                questionEntity.questionLevel.in(questionLevels),
                questionCategoryEntity.id.in(questionCategoryIds),
                questionEntity.questionType.eq(questionType))
            .fetch();
    }

    @Override
    public QuestionDetail getQuestionDetail(Long questionId) {
        QQuestionCategoryEntity parent = new QQuestionCategoryEntity("parent");
        QQuestionCategoryEntity child = new QQuestionCategoryEntity("child");
        QuestionDetail questionDetail = jpaQueryFactory.select(
                Projections.constructor(
                    QuestionDetail.class,
                    questionEntity.id,
                    questionEntity.title,
                    userEntity.name,
                    questionEntity.subject,
                    parent.title,
                    child.title,
                    questionEntity.questionLevel,
                    questionEntity.createdAt,
                    questionEntity.price
                ))
            .from(questionEntity)
            .where(questionEntity.id.eq(questionId))
            .leftJoin(userEntity).on(userEntity.uid.eq(questionEntity.creatorId))
            .leftJoin(child).on(child.id.eq(questionEntity.questionCategoryId))
            .leftJoin(parent).on(parent.id.eq(child.parentId))
            .fetchFirst();

        if (questionDetail == null) {
            throw new CustomException(Error.NOT_FOUND);
        }

        return questionDetail;
    }

    @Override
    public List<Question> getQuestionListInIds(List<Long> questionIds) {
        return jpaQueryFactory.select(questionEntity)
            .from(questionEntity)
            .where(questionEntity.id.in(questionIds))
            .fetch()
            .stream()
            .map(QuestionEntity::toDomain)
            .collect(Collectors.toList());
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
