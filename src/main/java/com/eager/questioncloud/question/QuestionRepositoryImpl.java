package com.eager.questioncloud.question;

import static com.eager.questioncloud.creator.QCreatorEntity.creatorEntity;
import static com.eager.questioncloud.library.QUserQuestionLibraryEntity.userQuestionLibraryEntity;
import static com.eager.questioncloud.question.QQuestionCategoryEntity.questionCategoryEntity;
import static com.eager.questioncloud.question.QQuestionEntity.questionEntity;
import static com.eager.questioncloud.review.QQuestionReviewEntity.questionReviewEntity;
import static com.eager.questioncloud.user.QUserEntity.userEntity;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.question.QuestionDto.QuestionInformation;
import com.eager.questioncloud.question.QuestionDto.QuestionInformationForWorkSpace;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.MathExpressions;
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
    private final QuestionJpaRepository questionJpaRepository;

    @Override
    public int getTotalFiltering(QuestionFilter questionFilter) {
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
    public List<QuestionInformation> getQuestionListByFiltering(QuestionFilter questionFilter) {
        QQuestionCategoryEntity parent = new QQuestionCategoryEntity("parent");
        QQuestionCategoryEntity child = new QQuestionCategoryEntity("child");
        return jpaQueryFactory.select(
                Projections.constructor(
                    QuestionInformation.class,
                    questionEntity.id,
                    questionEntity.title,
                    parent.title,
                    child.title,
                    questionEntity.thumbnail,
                    userEntity.name,
                    questionEntity.questionLevel,
                    questionEntity.price,
                    MathExpressions.round(questionReviewEntity.rate.avg(), 1).coalesce(0.0),
                    userQuestionLibraryEntity.id.isNotNull()))
            .from(questionEntity)
            .offset(questionFilter.getPageable().getOffset())
            .limit(questionFilter.getPageable().getPageSize())
            .innerJoin(child).on(child.id.eq(questionEntity.questionCategoryId))
            .innerJoin(parent).on(parent.id.eq(child.parentId))
            .innerJoin(creatorEntity).on(creatorEntity.id.eq(questionEntity.creatorId))
            .innerJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .innerJoin(questionCategoryEntity).on(questionCategoryEntity.id.eq(questionEntity.questionCategoryId))
            .leftJoin(userQuestionLibraryEntity)
            .on(userQuestionLibraryEntity.questionId.eq(questionEntity.id), userQuestionLibraryEntity.userId.eq(questionFilter.getUserId()))
            .leftJoin(questionReviewEntity).on(questionReviewEntity.questionId.eq(questionEntity.id))
            .groupBy(questionEntity.id)
            .orderBy(sort(questionFilter.getSort()), questionEntity.id.desc())
            .where(
                questionLevelFilter(questionFilter.getLevels()),
                questionCategoryFilter(questionFilter.getCategories()),
                questionTypeFilter(questionFilter.getQuestionType()),
                questionCreatorFilter(questionFilter.getCreatorId()),
                questionStatusFilter())
            .fetch();
    }

    @Override
    public QuestionInformation getQuestionInformation(Long questionId, Long userId) {
        QQuestionCategoryEntity parent = new QQuestionCategoryEntity("parent");
        QQuestionCategoryEntity child = new QQuestionCategoryEntity("child");
        QuestionInformation questionInformation = jpaQueryFactory.select(
                Projections.constructor(
                    QuestionInformation.class,
                    questionEntity.id,
                    questionEntity.title,
                    parent.title,
                    child.title,
                    questionEntity.thumbnail,
                    userEntity.name,
                    questionEntity.questionLevel,
                    questionEntity.price,
                    MathExpressions.round(questionReviewEntity.rate.avg(), 1).coalesce(0.0),
                    userQuestionLibraryEntity.id.isNotNull()))
            .from(questionEntity)
            .innerJoin(child).on(child.id.eq(questionEntity.questionCategoryId))
            .innerJoin(parent).on(parent.id.eq(child.parentId))
            .innerJoin(creatorEntity).on(creatorEntity.id.eq(questionEntity.creatorId))
            .innerJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .innerJoin(questionCategoryEntity).on(questionCategoryEntity.id.eq(questionEntity.questionCategoryId))
            .leftJoin(userQuestionLibraryEntity)
            .on(userQuestionLibraryEntity.questionId.eq(questionEntity.id), userQuestionLibraryEntity.userId.eq(userId))
            .leftJoin(questionReviewEntity).on(questionReviewEntity.questionId.eq(questionEntity.id))
            .groupBy(questionEntity.id)
            .where(questionEntity.id.eq(questionId))
            .fetchFirst();

        if (questionInformation == null) {
            throw new CustomException(Error.NOT_FOUND);
        }

        return questionInformation;
    }

    @Override
    public List<Question> getQuestionListInIds(List<Long> questionIds) {
        return jpaQueryFactory.select(questionEntity)
            .from(questionEntity)
            .where(questionEntity.id.in(questionIds), questionStatusFilter())
            .fetch()
            .stream()
            .map(QuestionEntity::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Boolean isAvailable(Long questionId) {
        Long result = jpaQueryFactory.select(questionEntity.id)
            .from(questionEntity)
            .where(questionEntity.id.eq(questionId))
            .fetchFirst();

        return result != null;
    }

    @Override
    public Question getForModifyAndDelete(Long questionId, Long creatorId) {
        return questionJpaRepository.findByIdAndCreatorId(questionId, creatorId)
            .orElseThrow(() -> new CustomException(Error.NOT_FOUND))
            .toDomain();
    }

    @Override
    public Question get(Long questionId) {
        return questionJpaRepository.findById(questionId)
            .filter(question -> !question.getQuestionStatus().equals(QuestionStatus.Delete))
            .map(QuestionEntity::toDomain)
            .orElseThrow(() -> new CustomException(Error.NOT_FOUND));
    }

    @Override
    public Question save(Question question) {
        return questionJpaRepository.save(question.toEntity()).toDomain();
    }

    @Override
    public List<QuestionInformationForWorkSpace> getCreatorQuestion(Long creatorId, Pageable pageable) {
        QQuestionCategoryEntity parent = new QQuestionCategoryEntity("parent");
        QQuestionCategoryEntity child = new QQuestionCategoryEntity("child");
        return jpaQueryFactory.select(
                Projections.constructor(
                    QuestionInformationForWorkSpace.class,
                    questionEntity.id,
                    questionEntity.title,
                    parent.title,
                    child.title,
                    questionEntity.thumbnail,
                    questionEntity.questionLevel,
                    questionEntity.price))
            .from(questionEntity)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .innerJoin(child).on(child.id.eq(questionEntity.questionCategoryId))
            .innerJoin(parent).on(parent.id.eq(child.parentId))
            .innerJoin(questionCategoryEntity).on(questionCategoryEntity.id.eq(questionEntity.questionCategoryId))
            .where(questionEntity.creatorId.eq(creatorId))
            .fetch();
    }

    @Override
    public int countCreatorQuestion(Long creatorId) {
        Integer result = jpaQueryFactory.select(questionEntity.id.count().intValue())
            .from(questionEntity)
            .where(questionEntity.creatorId.eq(creatorId))
            .fetchFirst();

        if (result == null) {
            return 0;
        }

        return result;
    }

    private OrderSpecifier<?> sort(QuestionSortType sort) {
        switch (sort) {
            case Popularity -> {
                return questionEntity.count.desc();
            }
            case Rate -> {
                return questionReviewEntity.rate.avg().desc();
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

    private BooleanExpression questionLevelFilter(List<QuestionLevel> levels) {
        if (levels == null || levels.isEmpty()) {
            return null;
        }
        return questionEntity.questionLevel.in(levels);
    }

    private BooleanExpression questionCategoryFilter(List<Long> categories) {
        if (categories == null || categories.isEmpty()) {
            return null;
        }
        return questionEntity.questionCategoryId.in(categories);
    }

    private BooleanExpression questionTypeFilter(QuestionType questionType) {
        if (questionType == null) {
            return null;
        }
        return questionEntity.questionType.eq(questionType);
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
}
