package com.eager.questioncloud.board;

import static com.eager.questioncloud.board.QQuestionBoardEntity.questionBoardEntity;
import static com.eager.questioncloud.question.QQuestionEntity.questionEntity;
import static com.eager.questioncloud.user.QUserEntity.userEntity;

import com.eager.questioncloud.board.QuestionBoardDto.QuestionBoardDetail;
import com.eager.questioncloud.board.QuestionBoardDto.QuestionBoardListItem;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.question.QQuestionCategoryEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuestionBoardRepositoryImpl implements QuestionBoardRepository {
    private final QuestionBoardJpaRepository questionBoardJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<QuestionBoardListItem> getQuestionBoardList(Long questionId, Pageable pageable) {
        QQuestionCategoryEntity parent = new QQuestionCategoryEntity("parent");
        QQuestionCategoryEntity child = new QQuestionCategoryEntity("child");
        return jpaQueryFactory.select(
                Projections.constructor(QuestionBoardListItem.class,
                    questionBoardEntity.id,
                    questionBoardEntity.title,
                    parent.title,
                    child.title,
                    questionEntity.title,
                    userEntity.name,
                    questionBoardEntity.createdAt
                ))
            .from(questionBoardEntity)
            .where(questionBoardEntity.questionId.eq(questionId))
            .leftJoin(questionEntity).on(questionEntity.id.eq(questionBoardEntity.questionId))
            .leftJoin(child).on(child.id.eq(questionEntity.questionCategoryId))
            .leftJoin(parent).on(parent.id.eq(child.parentId))
            .leftJoin(userEntity).on(userEntity.uid.eq(questionBoardEntity.writerId))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }

    @Override
    public List<QuestionBoardListItem> getCreatorQuestionBoardList(Long creatorId, Pageable pageable) {
        QQuestionCategoryEntity parent = new QQuestionCategoryEntity("parent");
        QQuestionCategoryEntity child = new QQuestionCategoryEntity("child");
        return jpaQueryFactory.select(
                Projections.constructor(QuestionBoardListItem.class,
                    questionBoardEntity.id,
                    questionBoardEntity.title,
                    parent.title,
                    child.title,
                    questionEntity.title,
                    userEntity.name,
                    questionBoardEntity.createdAt
                ))
            .from(questionEntity)
            .where(questionEntity.creatorId.eq(creatorId))
            .innerJoin(questionBoardEntity).on(questionBoardEntity.questionId.eq(questionEntity.id))
            .innerJoin(child).on(child.id.eq(questionEntity.questionCategoryId))
            .innerJoin(parent).on(parent.id.eq(child.parentId))
            .innerJoin(userEntity).on(userEntity.uid.eq(questionBoardEntity.writerId))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }

    @Override
    public int countCreatorQuestionBoard(Long creatorId) {
        Integer result = jpaQueryFactory.select(questionBoardEntity.id.count().intValue())
            .from(questionEntity)
            .where(questionEntity.creatorId.eq(creatorId))
            .innerJoin(questionBoardEntity).on(questionBoardEntity.questionId.eq(questionEntity.id))
            .fetchFirst();

        if (result == null) {
            return 0;
        }

        return result;
    }

    @Override
    public QuestionBoardDetail getQuestionBoardDetail(Long boardId) {
        QQuestionCategoryEntity parent = new QQuestionCategoryEntity("parent");
        QQuestionCategoryEntity child = new QQuestionCategoryEntity("child");
        QuestionBoardDetail questionBoardDetail = jpaQueryFactory.select(
                Projections.constructor(QuestionBoardDetail.class,
                    questionBoardEntity.id,
                    questionBoardEntity.questionId,
                    questionBoardEntity.title,
                    questionBoardEntity.content,
                    questionBoardEntity.files,
                    parent.title,
                    child.title,
                    questionEntity.title,
                    userEntity.name,
                    questionBoardEntity.createdAt
                ))
            .from(questionBoardEntity)
            .where(questionBoardEntity.id.eq(boardId))
            .leftJoin(questionEntity).on(questionEntity.id.eq(questionBoardEntity.questionId))
            .leftJoin(child).on(child.id.eq(questionEntity.questionCategoryId))
            .leftJoin(parent).on(parent.id.eq(child.parentId))
            .leftJoin(userEntity).on(userEntity.uid.eq(questionBoardEntity.writerId))
            .fetchFirst();

        if (questionBoardDetail == null) {
            throw new CustomException(Error.NOT_FOUND);
        }

        return questionBoardDetail;
    }

    @Override
    public QuestionBoard getForModifyAndDelete(Long boardId, Long userId) {
        return questionBoardJpaRepository.findByIdAndWriterId(boardId, userId)
            .orElseThrow(() -> new CustomException(Error.NOT_FOUND))
            .toModel();
    }

    @Override
    public QuestionBoard get(Long boardId) {
        return questionBoardJpaRepository.findById(boardId)
            .orElseThrow(() -> new CustomException(Error.NOT_FOUND))
            .toModel();
    }

    @Override
    public int count(Long questionId) {
        Integer result = jpaQueryFactory.select(questionBoardEntity.id.count().intValue())
            .from(questionBoardEntity)
            .where(questionBoardEntity.questionId.eq(questionId))
            .fetchFirst();

        if (result == null) {
            return 0;
        }

        return result;
    }

    @Override
    public QuestionBoard save(QuestionBoard questionBoard) {
        return questionBoardJpaRepository.save(questionBoard.toEntity()).toModel();
    }

    @Override
    public void delete(QuestionBoard questionBoard) {
        questionBoardJpaRepository.delete(questionBoard.toEntity());
    }
}
