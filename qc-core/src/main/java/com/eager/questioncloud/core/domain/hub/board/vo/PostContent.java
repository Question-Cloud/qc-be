package com.eager.questioncloud.core.domain.hub.board.vo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostContent {
    private String title;
    private String content;
    private List<PostFile> files;

    public static PostContent create(String title, String content, List<PostFile> files) {
        return new PostContent(title, content, files);
    }
}
