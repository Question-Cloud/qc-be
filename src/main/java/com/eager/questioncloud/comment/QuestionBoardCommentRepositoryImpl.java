package com.eager.questioncloud.comment;

import static com.eager.questioncloud.board.QQuestionBoardEntity.questionBoardEntity;
import static com.eager.questioncloud.comment.QQuestionBoardCommentEntity.questionBoardCommentEntity;
import static com.eager.questioncloud.creator.QCreatorEntity.creatorEntity;
import static com.eager.questioncloud.question.QQuestionEntity.questionEntity;
import static com.eager.questioncloud.user.QUserEntity.userEntity;

import com.eager.questioncloud.comment.QuestionBoardCommentDto.QuestionBoardCommentDetail;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuestionBoardCommentRepositoryImpl implements QuestionBoardCommentRepository {
    private final QuestionBoardCommentJpaRepository questionBoardCommentJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public QuestionBoardComment append(QuestionBoardComment questionBoardComment) {
        return questionBoardCommentJpaRepository.save(questionBoardComment.toEntity()).toModel();
    }

    @Override
    public QuestionBoardComment save(QuestionBoardComment questionBoardComment) {
        return questionBoardCommentJpaRepository.save(questionBoardComment.toEntity()).toModel();
    }

    @Override
    public QuestionBoardComment getForModifyAndDelete(Long commentId, Long userId) {
        return questionBoardCommentJpaRepository.findByIdAndWriterId(commentId, userId)
            .orElseThrow(() -> new CustomException(Error.NOT_FOUND))
            .toModel();
    }

    @Override
    public List<QuestionBoardCommentDetail> getQuestionBoardCommentDetails(Long boardId, Pageable pageable) {
        Long questionCreatorUserId = getQuestionCreatorUserId(boardId);
        return jpaQueryFactory.select(
                questionBoardCommentEntity.id,
                userEntity.uid,
                userEntity.name,
                userEntity.profileImage,
                questionBoardCommentEntity.comment,
                questionBoardCommentEntity.createdAt
            )
            .from(questionBoardCommentEntity)
            .where(questionBoardCommentEntity.boardId.eq(boardId))
            .leftJoin(userEntity).on(userEntity.uid.eq(questionBoardCommentEntity.writerId))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .stream()
            .map(tuple -> QuestionBoardCommentDetail.builder()
                .id(tuple.get(questionBoardCommentEntity.id))
                .writerName(tuple.get(userEntity.name))
                .profileImage(tuple.get(userEntity.profileImage))
                .comment(tuple.get(questionBoardCommentEntity.comment))
                .isCreator(questionCreatorUserId.equals(tuple.get(userEntity.uid)))
                .createdAt(tuple.get(questionBoardCommentEntity.createdAt))
                .build())
            .collect(Collectors.toList());
    }

    @Override
    public void delete(QuestionBoardComment questionBoardComment) {
        questionBoardCommentJpaRepository.delete(questionBoardComment.toEntity());
    }

    @Override
    public int count(Long boardId) {
        Integer result = jpaQueryFactory.select(questionBoardCommentEntity.id.count().intValue())
            .from(questionBoardCommentEntity)
            .where(questionBoardCommentEntity.boardId.eq(boardId))
            .fetchFirst();

        if (result == null) {
            return 0;
        }

        return result;
    }

    private Long getQuestionCreatorUserId(Long boardId) {
        Long questionCreatorUserId = jpaQueryFactory.select(userEntity.uid)
            .from(questionBoardEntity)
            .innerJoin(questionEntity).on(questionEntity.id.eq(questionBoardEntity.questionId))
            .innerJoin(creatorEntity).on(creatorEntity.id.eq(questionEntity.creatorId))
            .innerJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .where(questionBoardEntity.id.eq(boardId))
            .fetchFirst();

        if (questionCreatorUserId == null) {
            throw new CustomException(Error.NOT_FOUND);
        }

        return questionCreatorUserId;
    }
}
