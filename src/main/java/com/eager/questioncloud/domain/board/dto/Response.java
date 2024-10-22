package com.eager.questioncloud.domain.board.dto;

import com.eager.questioncloud.domain.board.dto.QuestionBoardDto.QuestionBoardDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class Response {
    @Getter
    @AllArgsConstructor
    public static class QuestionBoardResponse {
        private QuestionBoardDetail board;
    }
}
