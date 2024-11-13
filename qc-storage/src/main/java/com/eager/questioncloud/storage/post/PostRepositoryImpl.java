package com.eager.questioncloud.storage.post;

import static com.eager.questioncloud.storage.post.QPostEntity.postEntity;
import static com.eager.questioncloud.storage.question.QQuestionEntity.questionEntity;
import static com.eager.questioncloud.storage.user.QUserEntity.userEntity;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.post.dto.PostDto.PostDetail;
import com.eager.questioncloud.core.domain.post.dto.PostDto.PostListItem;
import com.eager.questioncloud.core.domain.post.model.Post;
import com.eager.questioncloud.core.domain.post.repository.PostRepository;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import com.eager.questioncloud.storage.question.QQuestionCategoryEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {
    private final PostJpaRepository postJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PostListItem> getPostList(Long questionId, PagingInformation pagingInformation) {
        QQuestionCategoryEntity parent = new QQuestionCategoryEntity("parent");
        QQuestionCategoryEntity child = new QQuestionCategoryEntity("child");
        return jpaQueryFactory.select(
                Projections.constructor(PostListItem.class,
                    postEntity.id,
                    postEntity.postContentEntity.title,
                    parent.title,
                    child.title,
                    questionEntity.questionContentEntity.title,
                    userEntity.userInformationEntity.name,
                    postEntity.createdAt
                ))
            .from(postEntity)
            .where(postEntity.questionId.eq(questionId))
            .leftJoin(questionEntity).on(questionEntity.id.eq(postEntity.questionId))
            .leftJoin(child).on(child.id.eq(questionEntity.questionCategoryId))
            .leftJoin(parent).on(parent.id.eq(child.parentId))
            .leftJoin(userEntity).on(userEntity.uid.eq(postEntity.writerId))
            .offset(pagingInformation.getOffset())
            .limit(pagingInformation.getSize())
            .fetch();
    }

    @Override
    public List<PostListItem> getCreatorPostList(Long creatorId, PagingInformation pagingInformation) {
        QQuestionCategoryEntity parent = new QQuestionCategoryEntity("parent");
        QQuestionCategoryEntity child = new QQuestionCategoryEntity("child");
        return jpaQueryFactory.select(
                Projections.constructor(PostListItem.class,
                    postEntity.id,
                    postEntity.postContentEntity.title,
                    parent.title,
                    child.title,
                    questionEntity.questionContentEntity.title,
                    userEntity.userInformationEntity.name,
                    postEntity.createdAt
                ))
            .from(questionEntity)
            .where(questionEntity.creatorId.eq(creatorId))
            .innerJoin(postEntity).on(postEntity.questionId.eq(questionEntity.id))
            .innerJoin(child).on(child.id.eq(questionEntity.questionCategoryId))
            .innerJoin(parent).on(parent.id.eq(child.parentId))
            .innerJoin(userEntity).on(userEntity.uid.eq(postEntity.writerId))
            .offset(pagingInformation.getOffset())
            .limit(pagingInformation.getSize())
            .fetch();
    }

    @Override
    public int countCreatorPost(Long creatorId) {
        Integer result = jpaQueryFactory.select(postEntity.id.count().intValue())
            .from(questionEntity)
            .where(questionEntity.creatorId.eq(creatorId))
            .innerJoin(postEntity).on(postEntity.questionId.eq(questionEntity.id))
            .fetchFirst();

        if (result == null) {
            return 0;
        }

        return result;
    }

    @Override
    public PostDetail getPostDetail(Long postId) {
        QQuestionCategoryEntity parent = new QQuestionCategoryEntity("parent");
        QQuestionCategoryEntity child = new QQuestionCategoryEntity("child");
        PostDetail postDetail = jpaQueryFactory.select(
                Projections.constructor(PostDetail.class,
                    postEntity.id,
                    postEntity.questionId,
                    postEntity.postContentEntity.title,
                    postEntity.postContentEntity.content,
                    postEntity.postContentEntity.files,
                    parent.title,
                    child.title,
                    questionEntity.questionContentEntity.title,
                    userEntity.userInformationEntity.name,
                    postEntity.createdAt
                ))
            .from(postEntity)
            .where(postEntity.id.eq(postId))
            .leftJoin(questionEntity).on(questionEntity.id.eq(postEntity.questionId))
            .leftJoin(child).on(child.id.eq(questionEntity.questionCategoryId))
            .leftJoin(parent).on(parent.id.eq(child.parentId))
            .leftJoin(userEntity).on(userEntity.uid.eq(postEntity.writerId))
            .fetchFirst();

        if (postDetail == null) {
            throw new CustomException(Error.NOT_FOUND);
        }

        return postDetail;
    }

    @Override
    public Post findByIdAndWriterId(Long postId, Long userId) {
        return postJpaRepository.findByIdAndWriterId(postId, userId)
            .orElseThrow(() -> new CustomException(Error.NOT_FOUND))
            .toModel();
    }

    @Override
    public Post findById(Long postId) {
        return postJpaRepository.findById(postId)
            .orElseThrow(() -> new CustomException(Error.NOT_FOUND))
            .toModel();
    }

    @Override
    public int count(Long questionId) {
        Integer result = jpaQueryFactory.select(postEntity.id.count().intValue())
            .from(postEntity)
            .where(postEntity.questionId.eq(questionId))
            .fetchFirst();

        if (result == null) {
            return 0;
        }

        return result;
    }

    @Override
    public Post save(Post post) {
        return postJpaRepository.save(PostEntity.from(post)).toModel();
    }

    @Override
    public void delete(Post post) {
        postJpaRepository.delete(PostEntity.from(post));
    }
}
