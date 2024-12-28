package com.eager.questioncloud.core.domain.question.infrastructure.repository;

import static com.eager.questioncloud.core.domain.creator.infrastructure.entity.QCreatorEntity.creatorEntity;
import static com.eager.questioncloud.core.domain.question.infrastructure.entity.QQuestionEntity.questionEntity;
import static com.eager.questioncloud.core.domain.review.infrastructure.entity.QQuestionReviewStatisticsEntity.questionReviewStatisticsEntity;
import static com.eager.questioncloud.core.domain.user.infrastructure.entity.QUserEntity.userEntity;
import static com.eager.questioncloud.core.domain.userquestion.infrastructure.entity.QUserQuestionEntity.userQuestionEntity;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.question.common.QuestionFilter;
import com.eager.questioncloud.core.domain.question.dto.QuestionInformation;
import com.eager.questioncloud.core.domain.question.enums.QuestionLevel;
import com.eager.questioncloud.core.domain.question.enums.QuestionSortType;
import com.eager.questioncloud.core.domain.question.enums.QuestionStatus;
import com.eager.questioncloud.core.domain.question.enums.QuestionType;
import com.eager.questioncloud.core.domain.question.infrastructure.entity.QQuestionCategoryEntity;
import com.eager.questioncloud.core.domain.question.infrastructure.entity.QuestionEntity;
import com.eager.questioncloud.core.domain.question.model.Question;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class QuestionRepositoryImpl implements QuestionRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QuestionJpaRepository questionJpaRepository;
    private final QQuestionCategoryEntity parent = new QQuestionCategoryEntity("parent");
    private final QQuestionCategoryEntity child = new QQuestionCategoryEntity("child");

    @Override
    public int countByQuestionFilter(QuestionFilter questionFilter) {
        Integer total = jpaQueryFactory.select(questionEntity.id.count().intValue())
            .from(questionEntity)
            .where(
                questionLevelFilter(questionFilter.getLevels()),
                questionCategoryFilter(questionFilter.getCategories()),
                questionTypeFilter(questionFilter.getQuestionType()),
                questionCreatorFilter(questionFilter.getCreatorId()),
                questionStatusFilter())
            .fetchFirst();

        if (total == null) {
            return 0;
        }

        return total;
    }

    @Override
    public List<QuestionInformation> getQuestionInformation(QuestionFilter questionFilter) {
        List<Tuple> tuples = questionInformationSelectFrom()
            .offset(questionFilter.getPagingInformation().getOffset())
            .limit(questionFilter.getPagingInformation().getSize())
            .innerJoin(child).on(child.id.eq(questionEntity.questionContentEntity.questionCategoryId))
            .innerJoin(parent).on(parent.id.eq(child.parentId))
            .innerJoin(creatorEntity).on(creatorEntity.id.eq(questionEntity.creatorId))
            .innerJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .leftJoin(userQuestionEntity)
            .on(userQuestionEntity.questionId.eq(questionEntity.id), userQuestionEntity.userId.eq(questionFilter.getUserId()))
            .leftJoin(questionReviewStatisticsEntity).on(questionReviewStatisticsEntity.questionId.eq(questionEntity.id))
            .groupBy(questionEntity.id)
            .orderBy(sort(questionFilter.getSort()), questionEntity.id.desc())
            .where(
                questionLevelFilter(questionFilter.getLevels()),
                questionCategoryFilter(questionFilter.getCategories()),
                questionTypeFilter(questionFilter.getQuestionType()),
                questionCreatorFilter(questionFilter.getCreatorId()),
                questionStatusFilter())
            .fetch();

        return tuples.stream()
            .map(this::parseQuestionInformationTuple)
            .collect(Collectors.toList());
    }

    @Override
    public QuestionInformation getQuestionInformation(Long questionId, Long userId) {
        Tuple tuple = questionInformationSelectFrom()
            .innerJoin(child).on(child.id.eq(questionEntity.questionContentEntity.questionCategoryId))
            .innerJoin(parent).on(parent.id.eq(child.parentId))
            .innerJoin(creatorEntity).on(creatorEntity.id.eq(questionEntity.creatorId))
            .innerJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .leftJoin(userQuestionEntity)
            .on(userQuestionEntity.questionId.eq(questionEntity.id), userQuestionEntity.userId.eq(userId))
            .leftJoin(questionReviewStatisticsEntity).on(questionReviewStatisticsEntity.questionId.eq(questionEntity.id))
            .where(questionEntity.id.eq(questionId), questionStatusFilter())
            .fetchFirst();

        if (tuple == null || tuple.get(questionEntity.id) == null) {
            throw new CoreException(Error.NOT_FOUND);
        }

        return parseQuestionInformationTuple(tuple);
    }

    @Override
    public List<Question> getQuestionsByQuestionIds(List<Long> questionIds) {
        return jpaQueryFactory.select(questionEntity)
            .from(questionEntity)
            .where(questionEntity.id.in(questionIds), questionStatusFilter())
            .fetch()
            .stream()
            .map(QuestionEntity::toModel)
            .collect(Collectors.toList());
    }

    @Override
    public Boolean isAvailable(Long questionId) {
        Long result = jpaQueryFactory.select(questionEntity.id)
            .from(questionEntity)
            .where(questionEntity.id.eq(questionId), questionStatusFilter())
            .fetchFirst();

        return result != null;
    }

    @Override
    public Question findByQuestionIdAndCreatorId(Long questionId, Long creatorId) {
        QuestionEntity result = jpaQueryFactory.select(questionEntity)
            .from(questionEntity)
            .where(questionEntity.creatorId.eq(creatorId), questionEntity.id.eq(questionId), questionEntity.questionStatus.ne(QuestionStatus.Delete))
            .fetchFirst();

        if (result == null) {
            throw new CoreException(Error.NOT_FOUND);
        }

        return result.toModel();
    }

    @Override
    public Question get(Long questionId) {
        QuestionEntity result = jpaQueryFactory.select(questionEntity)
            .from(questionEntity)
            .where(questionEntity.id.eq(questionId), questionStatusFilter())
            .fetchFirst();

        if (result == null) {
            throw new CoreException(Error.UNAVAILABLE_QUESTION);
        }

        return result.toModel();
    }

    @Override
    public Question save(Question question) {
        return questionJpaRepository.save(QuestionEntity.from(question)).toModel();
    }

    @Override
    public List<QuestionInformation> findByCreatorIdWithPaging(Long creatorId, PagingInformation pagingInformation) {
        List<Tuple> tuples = questionInformationSelectFrom()
            .where(questionEntity.creatorId.eq(creatorId), questionEntity.questionStatus.ne(QuestionStatus.Delete))
            .innerJoin(child).on(child.id.eq(questionEntity.questionContentEntity.questionCategoryId))
            .innerJoin(parent).on(parent.id.eq(child.parentId))
            .innerJoin(creatorEntity).on(creatorEntity.id.eq(questionEntity.creatorId))
            .innerJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .leftJoin(questionReviewStatisticsEntity).on(questionReviewStatisticsEntity.questionId.eq(questionEntity.id))
            .leftJoin(userQuestionEntity)
            .on(userQuestionEntity.questionId.eq(questionEntity.id), userQuestionEntity.userId.eq(userEntity.uid))
            .fetch();

        return tuples.stream()
            .map(this::parseQuestionInformationTuple)
            .collect(Collectors.toList());
    }

    @Override
    public List<QuestionInformation> findByQuestionIdIn(List<Long> questionIds) {
        List<Tuple> tuples = questionInformationSelectFrom()
            .where(questionEntity.id.in(questionIds))
            .innerJoin(child).on(child.id.eq(questionEntity.questionContentEntity.questionCategoryId))
            .innerJoin(parent).on(parent.id.eq(child.parentId))
            .innerJoin(creatorEntity).on(creatorEntity.id.eq(questionEntity.creatorId))
            .innerJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .leftJoin(questionReviewStatisticsEntity).on(questionReviewStatisticsEntity.questionId.eq(questionEntity.id))
            .leftJoin(userQuestionEntity)
            .on(userQuestionEntity.questionId.eq(questionEntity.id), userQuestionEntity.userId.eq(userEntity.uid))
            .fetch();

        return tuples.stream()
            .map(this::parseQuestionInformationTuple)
            .collect(Collectors.toList());
    }

    @Override
    public int countByCreatorId(Long creatorId) {
        Integer result = jpaQueryFactory.select(questionEntity.id.count().intValue())
            .from(questionEntity)
            .where(questionEntity.creatorId.eq(creatorId), questionEntity.questionStatus.ne(QuestionStatus.Delete))
            .fetchFirst();

        if (result == null) {
            return 0;
        }

        return result;
    }

    @Override
    @Transactional
    public void increaseQuestionCount(Long questionId) {
        jpaQueryFactory.update(questionEntity)
            .set(questionEntity.count, questionEntity.count.add(1))
            .where(questionEntity.id.eq(questionId))
            .execute();
    }

    @Override
    public void deleteAllInBatch() {
        questionJpaRepository.deleteAllInBatch();
    }

    private OrderSpecifier<?> sort(QuestionSortType sort) {
        switch (sort) {
            case Popularity -> {
                return questionEntity.count.desc();
            }
            case Rate -> {
                return questionReviewStatisticsEntity.averageRate.desc();
            }
            case Latest -> {
                return questionEntity.createdAt.desc();
            }
            case LEVEL -> {
                return questionEntity.questionContentEntity.questionLevel.desc();
            }
            default -> {
                return null;
            }
        }
    }

    private BooleanExpression questionLevelFilter(List<QuestionLevel> levels) {
        if (levels == null || levels.isEmpty()) {
            return null;
        }
        return questionEntity.questionContentEntity.questionLevel.in(levels);
    }

    private BooleanExpression questionCategoryFilter(List<Long> categories) {
        if (categories == null || categories.isEmpty()) {
            return null;
        }
        return questionEntity.questionContentEntity.questionCategoryId.in(categories);
    }

    private BooleanExpression questionTypeFilter(QuestionType questionType) {
        if (questionType == null) {
            return null;
        }
        return questionEntity.questionContentEntity.questionType.eq(questionType);
    }

    private BooleanExpression questionCreatorFilter(Long creatorId) {
        if (creatorId == null) {
            return null;
        }
        return questionEntity.creatorId.eq(creatorId);
    }

    private BooleanExpression questionStatusFilter() {
        return questionEntity.questionStatus.ne(QuestionStatus.Delete).and(questionEntity.questionStatus.ne(QuestionStatus.UnAvailable));
    }

    private QuestionInformation parseQuestionInformationTuple(Tuple tuple) {
        return QuestionInformation.builder()
            .id(tuple.get(questionEntity.id))
            .title(tuple.get(questionEntity.questionContentEntity.title))
            .subject(tuple.get(questionEntity.questionContentEntity.subject))
            .parentCategory(tuple.get(parent.title))
            .childCategory(tuple.get(child.title))
            .thumbnail(tuple.get(questionEntity.questionContentEntity.thumbnail))
            .creatorName(tuple.get(userEntity.userInformationEntity.name))
            .questionLevel(tuple.get(questionEntity.questionContentEntity.questionLevel))
            .price(tuple.get(questionEntity.questionContentEntity.price))
            .rate(tuple.get(questionReviewStatisticsEntity.averageRate))
            .isOwned(tuple.get(userQuestionEntity.id.isNotNull()))
            .build();
    }

    private JPAQuery<Tuple> questionInformationSelectFrom() {
        return jpaQueryFactory.query()
            .select(
                questionEntity.id,
                questionEntity.questionContentEntity.title,
                questionEntity.count,
                parent.title,
                child.title,
                questionEntity.questionContentEntity.thumbnail,
                userEntity.userInformationEntity.name,
                questionEntity.questionContentEntity.questionLevel,
                questionEntity.questionContentEntity.price,
                userQuestionEntity.id.isNotNull(),
                questionReviewStatisticsEntity.averageRate,
                questionEntity.questionContentEntity.subject)
            .from(questionEntity);
    }
}
