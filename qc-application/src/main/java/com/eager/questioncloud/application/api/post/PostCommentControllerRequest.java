package com.eager.questioncloud.application.api.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class PostCommentControllerRequest {
    @Getter
    public static class AddPostCommentRequest {
        @NotNull
        private Long postId;

        @NotBlank
        private String comment;
    }

    @Getter
    public static class ModifyPostCommentRequest {
        @NotBlank
        private String comment;
    }
}
