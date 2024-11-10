package com.eager.questioncloud.core.domain.post.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

public class PostCommentDto {
    @Getter
    @Builder
    public static class PostCommentDetail {
        private Long id;
        private String writerName;
        private String profileImage;
        private String comment;
        private Boolean isCreator;
        private Boolean isWriter;
        private LocalDateTime createdAt;
    }
}
