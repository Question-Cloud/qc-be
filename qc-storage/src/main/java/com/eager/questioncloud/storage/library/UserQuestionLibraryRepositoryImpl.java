package com.eager.questioncloud.storage.library;

import static com.eager.questioncloud.storage.creator.QCreatorEntity.creatorEntity;
import static com.eager.questioncloud.storage.library.QUserQuestionLibraryEntity.userQuestionLibraryEntity;
import static com.eager.questioncloud.storage.question.QQuestionEntity.questionEntity;
import static com.eager.questioncloud.storage.user.QUserEntity.userEntity;

import com.eager.questioncloud.core.domain.library.dto.UserQuestionLibraryDto.UserQuestionLibraryItem;
import com.eager.questioncloud.core.domain.library.model.UserQuestionLibrary;
import com.eager.questioncloud.core.domain.library.repository.UserQuestionLibraryRepository;
import com.eager.questioncloud.core.domain.questionhub.question.common.QuestionFilter;
import com.eager.questioncloud.core.domain.questionhub.question.dto.QuestionDto.QuestionInformationForLibrary;
import com.eager.questioncloud.storage.question.QQuestionCategoryEntity;
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
    public List<UserQuestionLibrary> saveAll(List<UserQuestionLibrary> userQuestionLibraries) {
        return UserQuestionLibraryEntity.toModel(userQuestionLibraryJpaRepository.saveAll(UserQuestionLibraryEntity.from(userQuestionLibraries)));
    }

    @Override
    public Boolean isOwned(Long userId, List<Long> questionIds) {
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
                        questionEntity.questionContentEntity.title,
                        parent.title,
                        child.title,
                        questionEntity.questionContentEntity.thumbnail,
                        userEntity.userInformationEntity.name,
                        questionEntity.questionContentEntity.questionLevel,
                        questionEntity.questionContentEntity.fileUrl,
                        questionEntity.questionContentEntity.explanationUrl)))
            .from(userQuestionLibraryEntity)
            .where(userQuestionLibraryEntity.userId.eq(questionFilter.getUserId()))
            .innerJoin(questionEntity).on(questionEntityJoinCondition(questionFilter))
            .innerJoin(child).on(child.id.eq(questionEntity.questionContentEntity.questionCategoryId))
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
            .innerJoin(child).on(child.id.eq(questionEntity.questionContentEntity.questionCategoryId))
            .innerJoin(parent).on(parent.id.eq(child.parentId))
            .innerJoin(creatorEntity).on(creatorEntity.id.eq(questionEntity.creatorId))
            .innerJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .offset(questionFilter.getPagingInformation().getPage())
            .limit(questionFilter.getPagingInformation().getSize())
            .fetchFirst();

        if (count == null) {
            return 0;
        }

        return count;
    }

    private BooleanBuilder questionEntityJoinCondition(QuestionFilter questionFilter) {
        BooleanBuilder builder = new BooleanBuilder();
        if (questionFilter.getLevels() != null && !questionFilter.getLevels().isEmpty()) {
            builder.and(questionEntity.questionContentEntity.questionLevel.in(questionFilter.getLevels()));
        }

        if (questionFilter.getCategories() != null && !questionFilter.getCategories().isEmpty()) {
            builder.and(questionEntity.questionContentEntity.questionCategoryId.in(questionFilter.getCategories()));
        }

        if (questionFilter.getQuestionType() != null) {
            builder.and(questionEntity.questionContentEntity.questionType.eq(questionFilter.getQuestionType()));
        }

        builder.and(questionEntity.id.eq(userQuestionLibraryEntity.questionId));

        return builder;
    }
}
