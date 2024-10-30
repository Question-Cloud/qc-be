package com.eager.questioncloud.core.domain.hub.board.vo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionBoardContent {
    private String title;
    private String content;
    private List<QuestionBoardFile> files;

    public static QuestionBoardContent create(String title, String content, List<QuestionBoardFile> files) {
        return new QuestionBoardContent(title, content, files);
    }
}