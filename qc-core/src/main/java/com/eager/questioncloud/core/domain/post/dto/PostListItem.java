package com.eager.questioncloud.core.domain.post.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostListItem {
    private Long id;
    private String title;
    private String writer;
    private LocalDateTime createdAt;
}
