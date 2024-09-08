package com.eager.questioncloud.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuestionBoardRepositoryImpl implements QuestionBoardRepository {
    private final QuestionBoardJpaRepository questionBoardJpaRepository;

    @Override
    public QuestionBoard append(QuestionBoard questionBoard) {
        return questionBoardJpaRepository.save(questionBoard.toEntity()).toModel();
    }
}
