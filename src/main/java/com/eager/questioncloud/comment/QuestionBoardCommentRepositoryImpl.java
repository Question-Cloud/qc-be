package com.eager.questioncloud.comment;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuestionBoardCommentRepositoryImpl implements QuestionBoardCommentRepository {
    private final QuestionBoardCommentJpaRepository questionBoardCommentJpaRepository;

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
    public void delete(QuestionBoardComment questionBoardComment) {
        questionBoardCommentJpaRepository.delete(questionBoardComment.toEntity());
    }
}
