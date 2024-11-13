package com.eager.questioncloud.storage.question;

import static com.eager.questioncloud.storage.creator.QCreatorEntity.creatorEntity;
import static com.eager.questioncloud.storage.question.QQuestionEntity.questionEntity;
import static com.eager.questioncloud.storage.question.QQuestionReviewStatisticsEntity.questionReviewStatisticsEntity;
import static com.eager.questioncloud.storage.user.QUserEntity.userEntity;

import com.eager.questioncloud.core.domain.question.common.QuestionFilter;
import com.eager.questioncloud.core.domain.question.common.QuestionSortType;
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
    public List<Question> getQuestionListInIds(List<Long> questionIds) {
        return jpaQueryFactory.select(questionEntity, creatorEntity, userEntity, parent, child)
            .from(questionEntity)
            .where(questionEntity.id.in(questionIds), questionStatusFilter())
            .leftJoin(creatorEntity).on(creatorEntity.id.eq(questionEntity.creatorId))
            .leftJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .innerJoin(child).on(child.id.eq(questionEntity.questionCategoryId))
            .innerJoin(parent).on(parent.id.eq(child.parentId))
            .fetch()
            .stream()
            .map(this::parseQuestionTuple)
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
        Tuple tuple = jpaQueryFactory.select(questionEntity, creatorEntity, userEntity, parent, child)
            .from(questionEntity)
            .where(questionEntity.id.eq(questionId), questionEntity.creatorId.eq(creatorId), questionStatusFilter())
            .leftJoin(creatorEntity).on(creatorEntity.id.eq(questionEntity.creatorId))
            .leftJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .innerJoin(child).on(child.id.eq(questionEntity.questionCategoryId))
            .innerJoin(parent).on(parent.id.eq(child.parentId))
            .fetchFirst();

        if (tuple == null) {
            throw new CustomException(Error.NOT_FOUND);
        }

        return parseQuestionTuple(tuple);
    }

    @Override
    public Question get(Long questionId) {
        QQuestionCategoryEntity parent = new QQuestionCategoryEntity("parent");
        QQuestionCategoryEntity child = new QQuestionCategoryEntity("child");

        Tuple tuple = jpaQueryFactory.select(questionEntity, creatorEntity, userEntity, parent, child)
            .from(questionEntity)
            .where(questionEntity.id.eq(questionId), questionStatusFilter())
            .leftJoin(creatorEntity).on(creatorEntity.id.eq(questionEntity.creatorId))
            .leftJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .innerJoin(child).on(child.id.eq(questionEntity.questionCategoryId))
            .innerJoin(parent).on(parent.id.eq(child.parentId))
            .fetchFirst();

        if (tuple == null) {
            throw new CustomException(Error.NOT_FOUND);
        }

        return parseQuestionTuple(tuple);
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
        return jpaQueryFactory.select(questionEntity, creatorEntity, userEntity, parent, child)
            .from(questionEntity)
            .where(
                questionLevelFilter(questionFilter.getLevels()),
                questionCategoryFilter(questionFilter.getCategories()),
                questionTypeFilter(questionFilter.getQuestionType()),
                questionCreatorFilter(questionFilter.getCreatorId()),
                questionStatusFilter())
            .leftJoin(creatorEntity).on(creatorEntity.id.eq(questionEntity.creatorId))
            .leftJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .innerJoin(child).on(child.id.eq(questionEntity.questionCategoryId))
            .innerJoin(parent).on(parent.id.eq(child.parentId))
            .orderBy(sort(questionFilter.getSort()))
            .fetch()
            .stream()
            .map(this::parseQuestionTuple)
            .collect(Collectors.toList());
    }

    private OrderSpecifier<?> sort(QuestionSortType sort) {
        if (sort == null) {
            return questionEntity.id.desc();
        }
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
            default -> throw new CustomException(Error.BAD_REQUEST);
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

    private Question parseQuestionTuple(Tuple tuple) {
        return Question.builder()
            .id(tuple.get(questionEntity).getId())
            .creator(tuple.get(creatorEntity).toModel(tuple.get(userEntity)))
            .category(new QuestionCategoryInformation(tuple.get(parent).toModel(), tuple.get(child).toModel()))
            .questionContent(tuple.get(questionEntity).getQuestionContentEntity().toModel())
            .questionStatus(tuple.get(questionEntity).getQuestionStatus())
            .count(tuple.get(questionEntity).getCount())
            .createdAt(tuple.get(questionEntity).getCreatedAt())
            .build();
    }
}
