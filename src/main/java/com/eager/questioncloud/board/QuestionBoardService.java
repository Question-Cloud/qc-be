package com.eager.questioncloud.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionBoardService {
    private final QuestionBoardRegister questionBoardRegister;

    public QuestionBoard register(QuestionBoard questionBoard) {
        return questionBoardRegister.register(questionBoard);
    }
}
