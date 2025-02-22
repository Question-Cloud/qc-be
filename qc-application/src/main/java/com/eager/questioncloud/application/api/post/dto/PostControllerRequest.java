package com.eager.questioncloud.application.api.post.dto;

import com.eager.questioncloud.core.domain.post.model.PostFile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

public class PostControllerRequest {
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
}
