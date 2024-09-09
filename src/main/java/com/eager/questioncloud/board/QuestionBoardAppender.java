package com.eager.questioncloud.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionBoardAppender {
    private final QuestionBoardRepository questionBoardRepository;

    public QuestionBoard append(QuestionBoard questionBoard) {
        return questionBoardRepository.append(questionBoard);
    }
}
