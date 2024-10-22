package com.eager.questioncloud.domain.board.vo;

import com.eager.questioncloud.domain.board.entity.converter.QuestionBoardFileConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class QuestionBoardContent {
    private String title;
    private String content;
    @Convert(converter = QuestionBoardFileConverter.class)
    private List<QuestionBoardFile> files;

    public static QuestionBoardContent create(String title, String content, List<QuestionBoardFile> files) {
        return new QuestionBoardContent(title, content, files);
    }
}
