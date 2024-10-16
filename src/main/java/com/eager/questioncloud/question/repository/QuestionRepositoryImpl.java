package com.eager.questioncloud.question.repository;

import static com.eager.questioncloud.creator.entity.QCreatorEntity.creatorEntity;
import static com.eager.questioncloud.library.entity.QUserQuestionLibraryEntity.userQuestionLibraryEntity;
import static com.eager.questioncloud.question.entity.QQuestionCategoryEntity.questionCategoryEntity;
import static com.eager.questioncloud.question.entity.QQuestionEntity.questionEntity;
import static com.eager.questioncloud.review.entity.QQuestionReviewStatisticsEntity.questionReviewStatisticsEntity;
import static com.eager.questioncloud.user.entity.QUserEntity.userEntity;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.question.common.QuestionFilter;
import com.eager.questioncloud.question.common.QuestionSortType;
import com.eager.questioncloud.question.dto.QuestionDto.QuestionInformation;
import com.eager.questioncloud.question.dto.QuestionDto.QuestionInformationForWorkSpace;
import com.eager.questioncloud.question.entity.QQuestionCategoryEntity;
import com.eager.questioncloud.question.entity.QuestionEntity;
import com.eager.questioncloud.question.model.Question;
import com.eager.questioncloud.question.vo.QuestionLevel;
import com.eager.questioncloud.question.vo.QuestionStatus;
import com.eager.questioncloud.question.vo.QuestionType;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
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
        List<Tuple> tuples = jpaQueryFactory.select(
                questionEntity.id,
                questionEntity.questionContent.title,
                questionEntity.count,
                parent.title,
                child.title,
                questionEntity.questionContent.thumbnail,
                userEntity.userInformation.name,
                questionEntity.questionContent.questionLevel,
                questionEntity.questionContent.price,
                userQuestionLibraryEntity.id.isNotNull(),
                questionReviewStatisticsEntity.averageRate)
            .from(questionEntity)
            .offset(questionFilter.getPageable().getOffset())
            .limit(questionFilter.getPageable().getPageSize())
            .innerJoin(child).on(child.id.eq(questionEntity.questionContent.questionCategoryId))
            .innerJoin(parent).on(parent.id.eq(child.parentId))
            .innerJoin(creatorEntity).on(creatorEntity.id.eq(questionEntity.creatorId))
            .innerJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .leftJoin(userQuestionLibraryEntity)
            .on(userQuestionLibraryEntity.questionId.eq(questionEntity.id), userQuestionLibraryEntity.userId.eq(questionFilter.getUserId()))
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
            .map(tuple -> QuestionInformation.builder()
                .id(tuple.get(questionEntity.id))
                .title(tuple.get(questionEntity.questionContent.title))
                .parentCategory(tuple.get(parent.title))
                .childCategory(tuple.get(child.title))
                .thumbnail(tuple.get(questionEntity.questionContent.thumbnail))
                .creatorName(tuple.get(userEntity.userInformation.name))
                .questionLevel(tuple.get(questionEntity.questionContent.questionLevel))
                .price(tuple.get(questionEntity.questionContent.price))
                .rate(tuple.get(questionReviewStatisticsEntity.averageRate))
                .isOwned(tuple.get(userQuestionLibraryEntity.id.isNotNull()))
                .build())
            .collect(Collectors.toList());
    }

    @Override
    public QuestionInformation getQuestionInformation(Long questionId, Long userId) {
        QQuestionCategoryEntity parent = new QQuestionCategoryEntity("parent");
        QQuestionCategoryEntity child = new QQuestionCategoryEntity("child");
        Tuple tuple = jpaQueryFactory.select(
                questionEntity.id,
                questionEntity.questionContent.title,
                questionEntity.count,
                parent.title,
                child.title,
                questionEntity.questionContent.thumbnail,
                userEntity.userInformation.name,
                questionEntity.questionContent.questionLevel,
                questionEntity.questionContent.price,
                userQuestionLibraryEntity.id.isNotNull(),
                questionReviewStatisticsEntity.averageRate)
            .from(questionEntity)
            .innerJoin(child).on(child.id.eq(questionEntity.questionContent.questionCategoryId))
            .innerJoin(parent).on(parent.id.eq(child.parentId))
            .innerJoin(creatorEntity).on(creatorEntity.id.eq(questionEntity.creatorId))
            .innerJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .leftJoin(userQuestionLibraryEntity)
            .on(userQuestionLibraryEntity.questionId.eq(questionEntity.id), userQuestionLibraryEntity.userId.eq(userId))
            .leftJoin(questionReviewStatisticsEntity).on(questionReviewStatisticsEntity.questionId.eq(questionEntity.id))
            .where(questionEntity.id.eq(questionId))
            .fetchFirst();

        if (tuple == null || tuple.get(questionEntity.id) == null) {
            throw new CustomException(Error.NOT_FOUND);
        }

        return QuestionInformation.builder()
            .id(tuple.get(questionEntity.id))
            .title(tuple.get(questionEntity.questionContent.title))
            .parentCategory(tuple.get(parent.title))
            .childCategory(tuple.get(child.title))
            .thumbnail(tuple.get(questionEntity.questionContent.thumbnail))
            .creatorName(tuple.get(userEntity.userInformation.name))
            .questionLevel(tuple.get(questionEntity.questionContent.questionLevel))
            .price(tuple.get(questionEntity.questionContent.price))
            .rate(tuple.get(questionReviewStatisticsEntity.averageRate))
            .isOwned(tuple.get(userQuestionLibraryEntity.id.isNotNull()))
            .build();
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
                    questionEntity.questionContent.title,
                    parent.title,
                    child.title,
                    questionEntity.questionContent.thumbnail,
                    questionEntity.questionContent.questionLevel,
                    questionEntity.questionContent.price))
            .from(questionEntity)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .innerJoin(child).on(child.id.eq(questionEntity.questionContent.questionCategoryId))
            .innerJoin(parent).on(parent.id.eq(child.parentId))
            .innerJoin(questionCategoryEntity).on(questionCategoryEntity.id.eq(questionEntity.questionContent.questionCategoryId))
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
                return questionReviewStatisticsEntity.averageRate.desc();
            }
            case Latest -> {
                return questionEntity.createdAt.desc();
            }
            case LEVEL -> {
                return questionEntity.questionContent.questionLevel.desc();
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
        return questionEntity.questionContent.questionLevel.in(levels);
    }

    private BooleanExpression questionCategoryFilter(List<Long> categories) {
        if (categories == null || categories.isEmpty()) {
            return null;
        }
        return questionEntity.questionContent.questionCategoryId.in(categories);
    }

    private BooleanExpression questionTypeFilter(QuestionType questionType) {
        if (questionType == null) {
            return null;
        }
        return questionEntity.questionContent.questionType.eq(questionType);
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
