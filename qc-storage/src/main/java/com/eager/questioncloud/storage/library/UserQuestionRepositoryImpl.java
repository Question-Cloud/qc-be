package com.eager.questioncloud.storage.library;

import static com.eager.questioncloud.storage.creator.QCreatorEntity.creatorEntity;
import static com.eager.questioncloud.storage.library.QUserQuestionEntity.userQuestionEntity;
import static com.eager.questioncloud.storage.question.QQuestionEntity.questionEntity;
import static com.eager.questioncloud.storage.user.QUserEntity.userEntity;

import com.eager.questioncloud.core.domain.library.model.UserQuestion;
import com.eager.questioncloud.core.domain.library.repository.UserQuestionRepository;
import com.eager.questioncloud.core.domain.question.common.QuestionFilter;
import com.eager.questioncloud.core.domain.question.model.Question;
import com.eager.questioncloud.core.domain.question.model.QuestionCategoryInformation;
import com.eager.questioncloud.storage.question.QQuestionCategoryEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserQuestionRepositoryImpl implements UserQuestionRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final UserQuestionJpaRepository userQuestionJpaRepository;
    private final QQuestionCategoryEntity parent = new QQuestionCategoryEntity("parent");
    private final QQuestionCategoryEntity child = new QQuestionCategoryEntity("child");

    @Override
    public void saveAll(List<UserQuestion> userQuestionLibraries) {
        userQuestionJpaRepository.saveAll(UserQuestionEntity.from(userQuestionLibraries));
    }

    @Override
    public Boolean isOwned(Long userId, List<Long> questionIds) {
        return userQuestionJpaRepository.existsByUserIdAndQuestionIdIn(userId, questionIds);
    }

    @Override
    public Boolean isOwned(Long userId, Long questionId) {
        return userQuestionJpaRepository.existsByUserIdAndQuestionId(userId, questionId);
    }

    @Override
    public List<UserQuestion> getUserQuestions(QuestionFilter questionFilter) {
        List<Tuple> tuples = jpaQueryFactory.select(userQuestionEntity, questionEntity, creatorEntity, userEntity, parent, child)
            .from(userQuestionEntity)
            .where(userQuestionEntity.userId.eq(questionFilter.getUserId()))
            .innerJoin(questionEntity).on(questionEntityJoinCondition(questionFilter))
            .innerJoin(creatorEntity).on(creatorEntity.id.eq(questionEntity.creatorId))
            .innerJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .innerJoin(child).on(child.id.eq(questionEntity.questionCategoryId))
            .innerJoin(parent).on(parent.id.eq(child.parentId))
            .fetch();

        if (tuples.isEmpty()) {
            return new ArrayList<>();
        }

        return tuples.stream()
            .map(tuple -> tuple.get(userQuestionEntity).toModel(parseQuestionTuple(tuple)))
            .collect(Collectors.toList());
    }

    @Override
    public int countUserQuestions(QuestionFilter questionFilter) {
        Integer count = jpaQueryFactory.select(userQuestionEntity.id.count().intValue())
            .from(userQuestionEntity)
            .where(userQuestionEntity.userId.eq(questionFilter.getUserId()))
            .innerJoin(questionEntity).on(questionEntityJoinCondition(questionFilter))
            .innerJoin(child).on(child.id.eq(questionEntity.questionCategoryId))
            .innerJoin(parent).on(parent.id.eq(child.parentId))
            .innerJoin(creatorEntity).on(creatorEntity.id.eq(questionEntity.creatorId))
            .innerJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .offset(questionFilter.getPagingInformation().getOffset())
            .limit(questionFilter.getPagingInformation().getSize())
            .fetchFirst();

        if (count == null) {
            return 0;
        }

        return count;
    }

    @Override
    public Set<Long> checkIsOwned(Long userId, List<Long> questionIds) {
        List<Long> result = jpaQueryFactory.select(userQuestionEntity.questionId)
            .from(userQuestionEntity)
            .where(userQuestionEntity.userId.eq(userId), userQuestionEntity.questionId.in(questionIds))
            .fetch();

        return new HashSet<>(result);
    }

    private BooleanBuilder questionEntityJoinCondition(QuestionFilter questionFilter) {
        BooleanBuilder builder = new BooleanBuilder();
        if (questionFilter.getLevels() != null && !questionFilter.getLevels().isEmpty()) {
            builder.and(questionEntity.questionContentEntity.questionLevel.in(questionFilter.getLevels()));
        }

        if (questionFilter.getCategories() != null && !questionFilter.getCategories().isEmpty()) {
            builder.and(questionEntity.questionCategoryId.in(questionFilter.getCategories()));
        }

        if (questionFilter.getQuestionType() != null) {
            builder.and(questionEntity.questionContentEntity.questionType.eq(questionFilter.getQuestionType()));
        }

        builder.and(questionEntity.id.eq(userQuestionEntity.questionId));

        return builder;
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
