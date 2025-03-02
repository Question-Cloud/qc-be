package com.eager.questioncloud.core.domain.post.dto;

import com.eager.questioncloud.core.domain.post.model.PostFile;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostDetail {
    private Long id;
    private Long questionId;
    private String title;
    private String content;
    private List<PostFile> files;
    private String parentCategory;
    private String childCategory;
    private String questionTitle;
    private String writer;
    private LocalDateTime createdAt;
}
