package com.eager.questioncloud.library;

import static com.eager.questioncloud.creator.QCreatorEntity.creatorEntity;
import static com.eager.questioncloud.library.QUserQuestionLibraryEntity.userQuestionLibraryEntity;
import static com.eager.questioncloud.question.QQuestionEntity.questionEntity;
import static com.eager.questioncloud.user.QUserEntity.userEntity;

import com.eager.questioncloud.library.UserQuestionLibraryDto.UserQuestionLibraryItem;
import com.eager.questioncloud.question.QQuestionCategoryEntity;
import com.eager.questioncloud.question.QuestionDto.QuestionInformationForLibrary;
import com.eager.questioncloud.question.QuestionFilter;
import com.eager.questioncloud.question.QuestionLevel;
import com.eager.questioncloud.question.QuestionType;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
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
            .innerJoin(questionEntity).on(
                questionEntity.id.eq(userQuestionLibraryEntity.questionId),
                questionLevelFilter(questionFilter.getLevels()),
                questionCategoryFilter(questionFilter.getCategories()),
                questionTypeFilter(questionFilter.getQuestionType()))
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
            .innerJoin(questionEntity).on(
                questionEntity.id.eq(userQuestionLibraryEntity.questionId),
                questionLevelFilter(questionFilter.getLevels()),
                questionCategoryFilter(questionFilter.getCategories()),
                questionTypeFilter(questionFilter.getQuestionType()))
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
}
