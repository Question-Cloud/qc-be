package com.eager.questioncloud.question;

import com.eager.questioncloud.question.QuestionDto.QuestionFilterItem;
import java.util.List;
import lombok.AllArgsConstructor;

public class Response {
    @AllArgsConstructor
    public static class QuestionFilterResponse {
        private List<QuestionFilterItem> questions;
    }
}
