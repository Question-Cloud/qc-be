package com.eager.questioncloud.storage.question;

import com.eager.questioncloud.core.domain.hub.board.vo.PostContent;
import com.eager.questioncloud.core.domain.hub.board.vo.PostFile;
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
    private List<PostFile> files;

    public static QuestionBoardContentEntity from(PostContent postContent) {
        return new QuestionBoardContentEntity(postContent.getTitle(), postContent.getTitle(), postContent.getFiles());
    }

    public PostContent toModel() {
        return new PostContent(title, content, files);
    }
}
