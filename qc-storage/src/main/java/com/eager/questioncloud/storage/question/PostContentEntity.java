package com.eager.questioncloud.storage.question;

import com.eager.questioncloud.core.domain.hub.post.vo.PostContent;
import com.eager.questioncloud.core.domain.hub.post.vo.PostFile;
import com.eager.questioncloud.storage.question.converter.PostFileConverter;
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
public class PostContentEntity {
    private String title;
    private String content;
    @Convert(converter = PostFileConverter.class)
    private List<PostFile> files;

    public static PostContentEntity from(PostContent postContent) {
        return new PostContentEntity(postContent.getTitle(), postContent.getTitle(), postContent.getFiles());
    }

    public PostContent toModel() {
        return new PostContent(title, content, files);
    }
}
