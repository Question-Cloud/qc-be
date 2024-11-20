package com.eager.questioncloud.domain.post;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PostCommentDetail {
    private Long id;
    private String writerName;
    private String profileImage;
    private String comment;
    private Boolean isCreator;
    private Boolean isWriter;
    private LocalDateTime createdAt;
}
