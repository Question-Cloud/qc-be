package com.eager.questioncloud.board;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class QuestionBoardDto {
    @Getter
    @AllArgsConstructor
    public static class QuestionBoardListItem {
        private Long id;
        private String title;
        private String parentCategory;
        private String childCategory;
        private String questionTitle;
        private String writer;
        private LocalDateTime createdAt;
    }
}
