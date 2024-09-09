package com.eager.questioncloud.comment;

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
}
