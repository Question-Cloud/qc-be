package com.eager.questioncloud.library;

import static com.eager.questioncloud.creator.QCreatorEntity.creatorEntity;
import static com.eager.questioncloud.library.QUserQuestionLibraryEntity.userQuestionLibraryEntity;
import static com.eager.questioncloud.question.QQuestionEntity.questionEntity;
import static com.eager.questioncloud.user.QUserEntity.userEntity;

import com.eager.questioncloud.library.UserQuestionLibraryDto.UserQuestionLibraryItem;
import com.eager.questioncloud.question.QQuestionCategoryEntity;
import com.eager.questioncloud.question.QuestionDto.QuestionInformationForLibrary;
import com.eager.questioncloud.question.QuestionFilter;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserQuestionLibraryRepositoryImpl implements UserQuestionLibraryRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final UserQuestionLibraryJpaRepository userQuestionLibraryJpaRepository;

    @Override
    public List<UserQuestionLibrary> append(List<UserQuestionLibrary> userQuestionLibraries) {
        return UserQuestionLibraryEntity.toModel(userQuestionLibraryJpaRepository.saveAll(UserQuestionLibrary.toEntity(userQuestionLibraries)));
    }

    @Override
    public Boolean checkDuplicate(Long userId, List<Long> questionIds) {
        return userQuestionLibraryJpaRepository.existsByUserIdAndQuestionIdIn(userId, questionIds);
    }

    @Override
    public Boolean isOwned(Long userId, Long questionId) {
        return userQuestionLibraryJpaRepository.existsByUserIdAndQuestionId(userId, questionId);
    }

    @Override
    public List<UserQuestionLibraryItem> getUserQuestions(QuestionFilter questionFilter) {
        QQuestionCategoryEntity parent = new QQuestionCategoryEntity("parent");
        QQuestionCategoryEntity child = new QQuestionCategoryEntity("child");
        return jpaQueryFactory.select(
                Projections.constructor(UserQuestionLibraryItem.class,
                    userQuestionLibraryEntity.id,
                    userQuestionLibraryEntity.isUsed,
                    Projections.constructor(QuestionInformationForLibrary.class,
                        questionEntity.id,
                        questionEntity.title,
                        parent.title,
                        child.title,
                        questionEntity.thumbnail,
                        userEntity.name,
                        questionEntity.questionLevel,
                        questionEntity.fileUrl,
                        questionEntity.explanationUrl)))
            .from(userQuestionLibraryEntity)
            .where(userQuestionLibraryEntity.userId.eq(questionFilter.getUserId()))
            .innerJoin(questionEntity).on(questionEntityJoinCondition(questionFilter))
            .innerJoin(child).on(child.id.eq(questionEntity.questionCategoryId))
            .innerJoin(parent).on(parent.id.eq(child.parentId))
            .innerJoin(creatorEntity).on(creatorEntity.id.eq(questionEntity.creatorId))
            .innerJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .fetch();
    }

    @Override
    public int countUserQuestions(QuestionFilter questionFilter) {
        QQuestionCategoryEntity parent = new QQuestionCategoryEntity("parent");
        QQuestionCategoryEntity child = new QQuestionCategoryEntity("child");
        Integer count = jpaQueryFactory.select(userQuestionLibraryEntity.id.count().intValue())
            .from(userQuestionLibraryEntity)
            .where(userQuestionLibraryEntity.userId.eq(questionFilter.getUserId()))
            .innerJoin(questionEntity).on(questionEntityJoinCondition(questionFilter))
            .innerJoin(child).on(child.id.eq(questionEntity.questionCategoryId))
            .innerJoin(parent).on(parent.id.eq(child.parentId))
            .innerJoin(creatorEntity).on(creatorEntity.id.eq(questionEntity.creatorId))
            .innerJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .fetchFirst();

        if (count == null) {
            return 0;
        }

        return count;
    }

    private BooleanBuilder questionEntityJoinCondition(QuestionFilter questionFilter) {
        BooleanBuilder builder = new BooleanBuilder();
        if (questionFilter.getLevels() != null && !questionFilter.getLevels().isEmpty()) {
            builder.and(questionEntity.questionLevel.in(questionFilter.getLevels()));
        }

        if (questionFilter.getCategories() != null && !questionFilter.getCategories().isEmpty()) {
            builder.and(questionEntity.questionCategoryId.in(questionFilter.getCategories()));
        }

        if (questionFilter.getQuestionType() != null) {
            builder.and(questionEntity.questionType.eq(questionFilter.getQuestionType()));
        }

        builder.and(questionEntity.id.eq(userQuestionLibraryEntity.questionId));

        return builder;
    }
}
