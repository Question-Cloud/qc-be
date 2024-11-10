package com.eager.questioncloud.api.hub.board;

import com.eager.questioncloud.core.domain.hub.board.vo.PostFile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

public class Request {
    @Getter
    public static class RegisterPostRequest {
        @NotNull
        private Long questionId;

        @NotBlank
        private String title;

        @NotBlank
        private String content;

        private List<PostFile> files = new ArrayList<>();
    }

    @Getter
    public static class ModifyPostRequest {
        @NotBlank
        private String title;

        @NotBlank
        private String content;

        private List<PostFile> files = new ArrayList<>();
    }

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
