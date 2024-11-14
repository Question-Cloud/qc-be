package com.eager.questioncloud.storage.question;

import static com.eager.questioncloud.storage.creator.QCreatorEntity.creatorEntity;
import static com.eager.questioncloud.storage.library.QLibraryEntity.libraryEntity;
import static com.eager.questioncloud.storage.question.QQuestionEntity.questionEntity;
import static com.eager.questioncloud.storage.question.QQuestionReviewStatisticsEntity.questionReviewStatisticsEntity;
import static com.eager.questioncloud.storage.user.QUserEntity.userEntity;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.question.common.QuestionFilter;
import com.eager.questioncloud.core.domain.question.common.QuestionSortType;
import com.eager.questioncloud.core.domain.question.dto.QuestionDto.QuestionInformation;
import com.eager.questioncloud.core.domain.question.model.Question;
import com.eager.questioncloud.core.domain.question.model.QuestionCategoryInformation;
import com.eager.questioncloud.core.domain.question.repository.QuestionRepository;
import com.eager.questioncloud.core.domain.question.vo.QuestionLevel;
import com.eager.questioncloud.core.domain.question.vo.QuestionStatus;
import com.eager.questioncloud.core.domain.question.vo.QuestionType;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuestionRepositoryImpl implements QuestionRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QuestionJpaRepository questionJpaRepository;

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
    public List<QuestionInformation> getQuestionListByFiltering(QuestionFilter questionFilter) {
        QQuestionCategoryEntity parent = new QQuestionCategoryEntity("parent");
        QQuestionCategoryEntity child = new QQuestionCategoryEntity("child");
        List<Tuple> tuples = jpaQueryFactory.select(
                questionEntity.id,
                questionEntity.questionContentEntity.title,
                questionEntity.count,
                parent.title,
                child.title,
                questionEntity.questionContentEntity.thumbnail,
                userEntity.userInformationEntity.name,
                questionEntity.questionContentEntity.questionLevel,
                questionEntity.questionContentEntity.price,
                libraryEntity.id.isNotNull(),
                questionReviewStatisticsEntity.averageRate)
            .from(questionEntity)
            .offset(questionFilter.getPagingInformation().getOffset())
            .limit(questionFilter.getPagingInformation().getSize())
            .innerJoin(child).on(child.id.eq(questionEntity.questionCategoryId))
            .innerJoin(parent).on(parent.id.eq(child.parentId))
            .innerJoin(creatorEntity).on(creatorEntity.id.eq(questionEntity.creatorId))
            .innerJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .leftJoin(libraryEntity)
            .on(libraryEntity.questionId.eq(questionEntity.id), libraryEntity.userId.eq(questionFilter.getUserId()))
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
                .title(tuple.get(questionEntity.questionContentEntity.title))
                .parentCategory(tuple.get(parent.title))
                .childCategory(tuple.get(child.title))
                .thumbnail(tuple.get(questionEntity.questionContentEntity.thumbnail))
                .creatorName(tuple.get(userEntity.userInformationEntity.name))
                .questionLevel(tuple.get(questionEntity.questionContentEntity.questionLevel))
                .price(tuple.get(questionEntity.questionContentEntity.price))
                .rate(tuple.get(questionReviewStatisticsEntity.averageRate))
                .isOwned(tuple.get(libraryEntity.id.isNotNull()))
                .build())
            .collect(Collectors.toList());
    }

    @Override
    public QuestionInformation getQuestionInformation(Long questionId, Long userId) {
        QQuestionCategoryEntity parent = new QQuestionCategoryEntity("parent");
        QQuestionCategoryEntity child = new QQuestionCategoryEntity("child");
        Tuple tuple = jpaQueryFactory.select(
                questionEntity.id,
                questionEntity.questionContentEntity.title,
                questionEntity.count,
                parent.title,
                child.title,
                questionEntity.questionContentEntity.thumbnail,
                userEntity.userInformationEntity.name,
                questionEntity.questionContentEntity.questionLevel,
                questionEntity.questionContentEntity.price,
                libraryEntity.id.isNotNull(),
                questionReviewStatisticsEntity.averageRate)
            .from(questionEntity)
            .innerJoin(child).on(child.id.eq(questionEntity.questionCategoryId))
            .innerJoin(parent).on(parent.id.eq(child.parentId))
            .innerJoin(creatorEntity).on(creatorEntity.id.eq(questionEntity.creatorId))
            .innerJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .leftJoin(libraryEntity)
            .on(libraryEntity.questionId.eq(questionEntity.id), libraryEntity.userId.eq(userId))
            .leftJoin(questionReviewStatisticsEntity).on(questionReviewStatisticsEntity.questionId.eq(questionEntity.id))
            .where(questionEntity.id.eq(questionId))
            .fetchFirst();

        if (tuple == null || tuple.get(questionEntity.id) == null) {
            throw new CustomException(Error.NOT_FOUND);
        }

        return QuestionInformation.builder()
            .id(tuple.get(questionEntity.id))
            .title(tuple.get(questionEntity.questionContentEntity.title))
            .parentCategory(tuple.get(parent.title))
            .childCategory(tuple.get(child.title))
            .thumbnail(tuple.get(questionEntity.questionContentEntity.thumbnail))
            .creatorName(tuple.get(userEntity.userInformationEntity.name))
            .questionLevel(tuple.get(questionEntity.questionContentEntity.questionLevel))
            .price(tuple.get(questionEntity.questionContentEntity.price))
            .rate(tuple.get(questionReviewStatisticsEntity.averageRate))
            .isOwned(tuple.get(libraryEntity.id.isNotNull()))
            .build();
    }

    @Override
    public List<Question> getQuestionListInIds(List<Long> questionIds) {
        QQuestionCategoryEntity parent = new QQuestionCategoryEntity("parent");
        QQuestionCategoryEntity child = new QQuestionCategoryEntity("child");

        return jpaQueryFactory.select(questionEntity, creatorEntity, parent, child)
            .from(questionEntity)
            .where(questionEntity.id.in(questionIds), questionStatusFilter())
            .leftJoin(creatorEntity).on(creatorEntity.id.eq(questionEntity.creatorId))
            .innerJoin(child).on(child.id.eq(questionEntity.questionCategoryId))
            .innerJoin(parent).on(parent.id.eq(child.parentId))
            .fetch()
            .stream()
            .map(tuple ->
                Question.builder()
                    .id(tuple.get(questionEntity).getId())
                    .creator(tuple.get(creatorEntity).toModel())
                    .category(new QuestionCategoryInformation(tuple.get(parent).toModel(), tuple.get(child).toModel()))
                    .questionContent(tuple.get(questionEntity).getQuestionContentEntity().toModel())
                    .questionStatus(tuple.get(questionEntity).getQuestionStatus())
                    .count(tuple.get(questionEntity).getCount())
                    .createdAt(tuple.get(questionEntity).getCreatedAt())
                    .build()
            )
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
    public Question findByQuestionIdAndCreatorId(Long questionId, Long creatorId) {
        QQuestionCategoryEntity parent = new QQuestionCategoryEntity("parent");
        QQuestionCategoryEntity child = new QQuestionCategoryEntity("child");

        Tuple tuple = jpaQueryFactory.select(questionEntity, creatorEntity)
            .from(questionEntity)
            .where(questionEntity.id.eq(questionId), questionEntity.creatorId.eq(creatorId), questionStatusFilter())
            .leftJoin(creatorEntity).on(creatorEntity.id.eq(questionEntity.creatorId))
            .innerJoin(child).on(child.id.eq(questionEntity.questionCategoryId))
            .innerJoin(parent).on(parent.id.eq(child.parentId))
            .fetchFirst();

        if (tuple == null) {
            throw new CustomException(Error.NOT_FOUND);
        }

        return Question.builder()
            .id(tuple.get(questionEntity).getId())
            .creator(tuple.get(creatorEntity).toModel())
            .category(new QuestionCategoryInformation(tuple.get(parent).toModel(), tuple.get(child).toModel()))
            .questionContent(tuple.get(questionEntity).getQuestionContentEntity().toModel())
            .questionStatus(tuple.get(questionEntity).getQuestionStatus())
            .count(tuple.get(questionEntity).getCount())
            .createdAt(tuple.get(questionEntity).getCreatedAt())
            .build();
    }

    @Override
    public Question get(Long questionId) {
        QQuestionCategoryEntity parent = new QQuestionCategoryEntity("parent");
        QQuestionCategoryEntity child = new QQuestionCategoryEntity("child");

        Tuple tuple = jpaQueryFactory.select(questionEntity, creatorEntity, parent, child)
            .from(questionEntity)
            .where(questionEntity.id.eq(questionId), questionStatusFilter())
            .leftJoin(creatorEntity).on(creatorEntity.id.eq(questionEntity.creatorId))
            .innerJoin(child).on(child.id.eq(questionEntity.questionCategoryId))
            .innerJoin(parent).on(parent.id.eq(child.parentId))
            .fetchFirst();

        if (tuple == null) {
            throw new CustomException(Error.NOT_FOUND);
        }

        return Question.builder()
            .id(tuple.get(questionEntity).getId())
            .creator(tuple.get(creatorEntity).toModel())
            .category(new QuestionCategoryInformation(tuple.get(parent).toModel(), tuple.get(child).toModel()))
            .questionContent(tuple.get(questionEntity).getQuestionContentEntity().toModel())
            .questionStatus(tuple.get(questionEntity).getQuestionStatus())
            .count(tuple.get(questionEntity).getCount())
            .createdAt(tuple.get(questionEntity).getCreatedAt())
            .build();
    }

    @Override
    public Question save(Question question) {
        QuestionEntity questionEntity = questionJpaRepository.save(QuestionEntity.from(question));

        return Question.builder()
            .id(questionEntity.getId())
            .creator(question.getCreator())
            .category(question.getCategory())
            .questionContent(question.getQuestionContent())
            .questionStatus(question.getQuestionStatus())
            .count(question.getCount())
            .createdAt(question.getCreatedAt())
            .build();
    }

    @Override
    public List<QuestionInformation> findByCreatorIdWithPaging(Long creatorId, PagingInformation pagingInformation) {
        QQuestionCategoryEntity parent = new QQuestionCategoryEntity("parent");
        QQuestionCategoryEntity child = new QQuestionCategoryEntity("child");
        List<Tuple> tuples = jpaQueryFactory.select(
                questionEntity.id,
                questionEntity.questionContentEntity.title,
                questionEntity.count,
                parent.title,
                child.title,
                questionEntity.questionContentEntity.thumbnail,
                userEntity.userInformationEntity.name,
                questionEntity.questionContentEntity.questionLevel,
                questionEntity.questionContentEntity.price,
                questionReviewStatisticsEntity.averageRate)
            .from(questionEntity)
            .innerJoin(child).on(child.id.eq(questionEntity.questionCategoryId))
            .innerJoin(parent).on(parent.id.eq(child.parentId))
            .innerJoin(creatorEntity).on(creatorEntity.id.eq(questionEntity.creatorId))
            .innerJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .leftJoin(questionReviewStatisticsEntity).on(questionReviewStatisticsEntity.questionId.eq(questionEntity.id))
            .where(questionEntity.creatorId.eq(creatorId))
            .fetch();

        return tuples.stream()
            .map(tuple -> QuestionInformation.builder()
                .id(tuple.get(questionEntity.id))
                .title(tuple.get(questionEntity.questionContentEntity.title))
                .parentCategory(tuple.get(parent.title))
                .childCategory(tuple.get(child.title))
                .thumbnail(tuple.get(questionEntity.questionContentEntity.thumbnail))
                .creatorName(tuple.get(userEntity.userInformationEntity.name))
                .questionLevel(tuple.get(questionEntity.questionContentEntity.questionLevel))
                .price(tuple.get(questionEntity.questionContentEntity.price))
                .rate(tuple.get(questionReviewStatisticsEntity.averageRate))
                .isOwned(true)
                .build())
            .collect(Collectors.toList());
    }

    @Override
    public int countByCreatorId(Long creatorId) {
        Integer result = jpaQueryFactory.select(questionEntity.id.count().intValue())
            .from(questionEntity)
            .where(questionEntity.creatorId.eq(creatorId))
            .fetchFirst();

        if (result == null) {
            return 0;
        }

        return result;
    }

    @Override
    public List<Question> getQuestionsByFilter(QuestionFilter questionFilter) {
        QQuestionCategoryEntity parent = new QQuestionCategoryEntity("parent");
        QQuestionCategoryEntity child = new QQuestionCategoryEntity("child");

        return jpaQueryFactory.select(questionEntity, creatorEntity, parent, child)
            .from(questionEntity)
            .where(
                questionLevelFilter(questionFilter.getLevels()),
                questionCategoryFilter(questionFilter.getCategories()),
                questionTypeFilter(questionFilter.getQuestionType()),
                questionCreatorFilter(questionFilter.getCreatorId()),
                questionStatusFilter())
            .leftJoin(creatorEntity).on(creatorEntity.id.eq(questionEntity.creatorId))
            .innerJoin(child).on(child.id.eq(questionEntity.questionCategoryId))
            .innerJoin(parent).on(parent.id.eq(child.parentId))
            .orderBy(sort(questionFilter.getSort()))
            .fetch()
            .stream()
            .map(tuple ->
                Question.builder()
                    .id(tuple.get(questionEntity).getId())
                    .creator(tuple.get(creatorEntity).toModel())
                    .category(new QuestionCategoryInformation(tuple.get(parent).toModel(), tuple.get(child).toModel()))
                    .questionContent(tuple.get(questionEntity).getQuestionContentEntity().toModel())
                    .questionStatus(tuple.get(questionEntity).getQuestionStatus())
                    .count(tuple.get(questionEntity).getCount())
                    .createdAt(tuple.get(questionEntity).getCreatedAt())
                    .build()
            )
            .collect(Collectors.toList());
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
        return questionEntity.questionCategoryId.in(categories);
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
}
