package com.eager.questioncloud.core.domain.board.dto;

import com.eager.questioncloud.core.domain.board.vo.QuestionBoardFile;
import java.time.LocalDateTime;
import java.util.List;
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

    @Getter
    @AllArgsConstructor
    public static class QuestionBoardDetail {
        private Long id;
        private Long questionId;
        private String title;
        private String content;
        private List<QuestionBoardFile> files;
        private String parentCategory;
        private String childCategory;
        private String questionTitle;
        private String writer;
        private LocalDateTime createdAt;
    }
}
