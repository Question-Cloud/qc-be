package com.eager.questioncloud.storage.question;

import com.eager.questioncloud.core.domain.questionhub.board.vo.QuestionBoardContent;
import com.eager.questioncloud.core.domain.questionhub.board.vo.QuestionBoardFile;
import com.eager.questioncloud.storage.question.converter.QuestionBoardFileConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class QuestionBoardContentEntity {
    private String title;
    private String content;
    @Convert(converter = QuestionBoardFileConverter.class)
    private List<QuestionBoardFile> files;

    public static QuestionBoardContentEntity from(QuestionBoardContent questionBoardContent) {
        return new QuestionBoardContentEntity(questionBoardContent.getTitle(), questionBoardContent.getTitle(), questionBoardContent.getFiles());
    }

    public QuestionBoardContent toModel() {
        return new QuestionBoardContent(title, content, files);
    }
}
