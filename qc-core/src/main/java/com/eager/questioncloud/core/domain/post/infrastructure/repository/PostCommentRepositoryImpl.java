package com.eager.questioncloud.core.domain.post.infrastructure.repository;

import static com.eager.questioncloud.core.domain.creator.infrastructure.entity.QCreatorEntity.creatorEntity;
import static com.eager.questioncloud.core.domain.post.infrastructure.entity.QPostCommentEntity.postCommentEntity;
import static com.eager.questioncloud.core.domain.post.infrastructure.entity.QPostEntity.postEntity;
import static com.eager.questioncloud.core.domain.question.infrastructure.entity.QQuestionEntity.questionEntity;
import static com.eager.questioncloud.core.domain.user.infrastructure.QUserEntity.userEntity;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.post.dto.PostCommentDetail;
import com.eager.questioncloud.core.domain.post.infrastructure.entity.PostCommentEntity;
import com.eager.questioncloud.core.domain.post.model.PostComment;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostCommentRepositoryImpl implements PostCommentRepository {
    private final PostCommentJpaRepository postCommentJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public PostComment save(PostComment postComment) {
        return postCommentJpaRepository.save(PostCommentEntity.from(postComment)).toModel();
    }

    @Override
    public PostComment findByIdAndWriterId(Long commentId, Long userId) {
        return postCommentJpaRepository.findByIdAndWriterId(commentId, userId)
            .orElseThrow(() -> new CoreException(Error.NOT_FOUND))
            .toModel();
    }

    @Override
    public List<PostCommentDetail> getPostCommentDetails(Long postId, Long userId, PagingInformation pagingInformation) {
        Long questionCreatorUserId = getQuestionCreatorUserId(postId);
        return jpaQueryFactory.select(
                postCommentEntity.id,
                userEntity.uid,
                userEntity.userInformationEntity.name,
                userEntity.userInformationEntity.profileImage,
                postCommentEntity.comment,
                postCommentEntity.createdAt
            )
            .from(postCommentEntity)
            .where(postCommentEntity.postId.eq(postId))
            .leftJoin(userEntity).on(userEntity.uid.eq(postCommentEntity.writerId))
            .offset(pagingInformation.getOffset())
            .limit(pagingInformation.getSize())
            .stream()
            .map(tuple -> PostCommentDetail.builder()
                .id(tuple.get(postCommentEntity.id))
                .writerName(tuple.get(userEntity.userInformationEntity.name))
                .profileImage(tuple.get(userEntity.userInformationEntity.profileImage))
                .comment(tuple.get(postCommentEntity.comment))
                .isCreator(questionCreatorUserId.equals(tuple.get(userEntity.uid)))
                .isWriter(userId.equals(tuple.get(userEntity.uid)))
                .createdAt(tuple.get(postCommentEntity.createdAt))
                .build())
            .collect(Collectors.toList());
    }

    @Override
    public void delete(PostComment postComment) {
        postCommentJpaRepository.delete(PostCommentEntity.from(postComment));
    }

    @Override
    public int count(Long postId) {
        Integer result = jpaQueryFactory.select(postCommentEntity.id.count().intValue())
            .from(postCommentEntity)
            .where(postCommentEntity.postId.eq(postId))
            .fetchFirst();

        if (result == null) {
            return 0;
        }

        return result;
    }

    private Long getQuestionCreatorUserId(Long postId) {
        Long questionCreatorUserId = jpaQueryFactory.select(userEntity.uid)
            .from(postEntity)
            .innerJoin(questionEntity).on(questionEntity.id.eq(postEntity.questionId))
            .innerJoin(creatorEntity).on(creatorEntity.id.eq(questionEntity.creatorId))
            .innerJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .where(postEntity.id.eq(postId))
            .fetchFirst();

        if (questionCreatorUserId == null) {
            throw new CoreException(Error.NOT_FOUND);
        }

        return questionCreatorUserId;
    }
}
