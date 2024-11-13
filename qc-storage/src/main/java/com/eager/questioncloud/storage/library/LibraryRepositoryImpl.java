package com.eager.questioncloud.storage.library;

import static com.eager.questioncloud.storage.creator.QCreatorEntity.creatorEntity;
import static com.eager.questioncloud.storage.library.QLibraryEntity.libraryEntity;
import static com.eager.questioncloud.storage.question.QQuestionEntity.questionEntity;
import static com.eager.questioncloud.storage.user.QUserEntity.userEntity;

import com.eager.questioncloud.core.domain.library.dto.UserQuestionDto.UserQuestionItem;
import com.eager.questioncloud.core.domain.library.model.UserQuestion;
import com.eager.questioncloud.core.domain.library.repository.LibraryRepository;
import com.eager.questioncloud.core.domain.question.common.QuestionFilter;
import com.eager.questioncloud.core.domain.question.dto.QuestionDto.QuestionInformationForLibrary;
import com.eager.questioncloud.storage.question.QQuestionCategoryEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LibraryRepositoryImpl implements LibraryRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final LibraryJpaRepository libraryJpaRepository;

    @Override
    public List<UserQuestion> saveAll(List<UserQuestion> userQuestionLibraries) {
        return LibraryEntity.toModel(libraryJpaRepository.saveAll(LibraryEntity.from(userQuestionLibraries)));
    }

    @Override
    public Boolean isOwned(Long userId, List<Long> questionIds) {
        return libraryJpaRepository.existsByUserIdAndQuestionIdIn(userId, questionIds);
    }

    @Override
    public Boolean isOwned(Long userId, Long questionId) {
        return libraryJpaRepository.existsByUserIdAndQuestionId(userId, questionId);
    }

    @Override
    public List<UserQuestionItem> getUserQuestions(QuestionFilter questionFilter) {
        QQuestionCategoryEntity parent = new QQuestionCategoryEntity("parent");
        QQuestionCategoryEntity child = new QQuestionCategoryEntity("child");
        return jpaQueryFactory.select(
                Projections.constructor(UserQuestionItem.class,
                    libraryEntity.id,
                    libraryEntity.isUsed,
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
            .from(libraryEntity)
            .where(libraryEntity.userId.eq(questionFilter.getUserId()))
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
        Integer count = jpaQueryFactory.select(libraryEntity.id.count().intValue())
            .from(libraryEntity)
            .where(libraryEntity.userId.eq(questionFilter.getUserId()))
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
        List<Long> result = jpaQueryFactory.select(libraryEntity.questionId)
            .from(libraryEntity)
            .where(libraryEntity.userId.eq(userId), libraryEntity.questionId.in(questionIds))
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

        builder.and(questionEntity.id.eq(libraryEntity.questionId));

        return builder;
    }
}
