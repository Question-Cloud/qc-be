package com.eager.questioncloud.storage.question;

import static com.eager.questioncloud.storage.question.QQuestionBoardEntity.questionBoardEntity;
import static com.eager.questioncloud.storage.question.QQuestionEntity.questionEntity;
import static com.eager.questioncloud.storage.user.QUserEntity.userEntity;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.hub.board.dto.PostDto.PostDetail;
import com.eager.questioncloud.core.domain.hub.board.dto.PostDto.PostListItem;
import com.eager.questioncloud.core.domain.hub.board.model.Post;
import com.eager.questioncloud.core.domain.hub.board.repository.QuestionBoardRepository;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuestionBoardRepositoryImpl implements QuestionBoardRepository {
    private final QuestionBoardJpaRepository questionBoardJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PostListItem> getQuestionBoardList(Long questionId, PagingInformation pagingInformation) {
        QQuestionCategoryEntity parent = new QQuestionCategoryEntity("parent");
        QQuestionCategoryEntity child = new QQuestionCategoryEntity("child");
        return jpaQueryFactory.select(
                Projections.constructor(PostListItem.class,
                    questionBoardEntity.id,
                    questionBoardEntity.questionBoardContentEntity.title,
                    parent.title,
                    child.title,
                    questionEntity.questionContentEntity.title,
                    userEntity.userInformationEntity.name,
                    questionBoardEntity.createdAt
                ))
            .from(questionBoardEntity)
            .where(questionBoardEntity.questionId.eq(questionId))
            .leftJoin(questionEntity).on(questionEntity.id.eq(questionBoardEntity.questionId))
            .leftJoin(child).on(child.id.eq(questionEntity.questionContentEntity.questionCategoryId))
            .leftJoin(parent).on(parent.id.eq(child.parentId))
            .leftJoin(userEntity).on(userEntity.uid.eq(questionBoardEntity.writerId))
            .offset(pagingInformation.getPage())
            .limit(pagingInformation.getSize())
            .fetch();
    }

    @Override
    public List<PostListItem> getCreatorQuestionBoardList(Long creatorId, PagingInformation pagingInformation) {
        QQuestionCategoryEntity parent = new QQuestionCategoryEntity("parent");
        QQuestionCategoryEntity child = new QQuestionCategoryEntity("child");
        return jpaQueryFactory.select(
                Projections.constructor(PostListItem.class,
                    questionBoardEntity.id,
                    questionBoardEntity.questionBoardContentEntity.title,
                    parent.title,
                    child.title,
                    questionEntity.questionContentEntity.title,
                    userEntity.userInformationEntity.name,
                    questionBoardEntity.createdAt
                ))
            .from(questionEntity)
            .where(questionEntity.creatorId.eq(creatorId))
            .innerJoin(questionBoardEntity).on(questionBoardEntity.questionId.eq(questionEntity.id))
            .innerJoin(child).on(child.id.eq(questionEntity.questionContentEntity.questionCategoryId))
            .innerJoin(parent).on(parent.id.eq(child.parentId))
            .innerJoin(userEntity).on(userEntity.uid.eq(questionBoardEntity.writerId))
            .offset(pagingInformation.getPage())
            .limit(pagingInformation.getSize())
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
    public PostDetail getQuestionBoardDetail(Long boardId) {
        QQuestionCategoryEntity parent = new QQuestionCategoryEntity("parent");
        QQuestionCategoryEntity child = new QQuestionCategoryEntity("child");
        PostDetail postDetail = jpaQueryFactory.select(
                Projections.constructor(PostDetail.class,
                    questionBoardEntity.id,
                    questionBoardEntity.questionId,
                    questionBoardEntity.questionBoardContentEntity.title,
                    questionBoardEntity.questionBoardContentEntity.content,
                    questionBoardEntity.questionBoardContentEntity.files,
                    parent.title,
                    child.title,
                    questionEntity.questionContentEntity.title,
                    userEntity.userInformationEntity.name,
                    questionBoardEntity.createdAt
                ))
            .from(questionBoardEntity)
            .where(questionBoardEntity.id.eq(boardId))
            .leftJoin(questionEntity).on(questionEntity.id.eq(questionBoardEntity.questionId))
            .leftJoin(child).on(child.id.eq(questionEntity.questionContentEntity.questionCategoryId))
            .leftJoin(parent).on(parent.id.eq(child.parentId))
            .leftJoin(userEntity).on(userEntity.uid.eq(questionBoardEntity.writerId))
            .fetchFirst();

        if (postDetail == null) {
            throw new CustomException(Error.NOT_FOUND);
        }

        return postDetail;
    }

    @Override
    public Post findByIdAndWriterId(Long boardId, Long userId) {
        return questionBoardJpaRepository.findByIdAndWriterId(boardId, userId)
            .orElseThrow(() -> new CustomException(Error.NOT_FOUND))
            .toModel();
    }

    @Override
    public Post findById(Long boardId) {
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
    public Post save(Post post) {
        return questionBoardJpaRepository.save(QuestionBoardEntity.from(post)).toModel();
    }

    @Override
    public void delete(Post post) {
        questionBoardJpaRepository.delete(QuestionBoardEntity.from(post));
    }
}
