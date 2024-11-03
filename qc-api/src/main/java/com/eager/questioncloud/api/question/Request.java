package com.eager.questioncloud.api.question;

import com.eager.questioncloud.core.domain.hub.board.vo.PostFile;
import com.eager.questioncloud.core.domain.hub.question.vo.QuestionLevel;
import com.eager.questioncloud.core.domain.hub.question.vo.Subject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

public class Request {
    @Getter
    public static class SelfMadeQuestionRequest {
        @NotNull
        private Long questionCategoryId;

        @NotNull
        private Subject subject;

        @NotBlank
        private String title;

        @NotBlank
        private String description;

        @NotBlank
        private String thumbnail;

        @NotBlank
        private String fileUrl;

        @NotBlank
        private String explanationUrl;

        @NotNull
        private QuestionLevel questionLevel;

        @Min(value = 100)
        private int price;
    }

    @Getter
    public static class RegisterQuestionReviewRequest {
        @NotNull
        private Long questionId;

        @Min(value = 1)
        @Max(value = 5)
        private int rate;

        @NotBlank
        private String comment;
    }

    @Getter
    public static class ModifyQuestionReviewRequest {
        @Min(value = 1)
        @Max(value = 5)
        private int rate;

        @NotBlank
        private String comment;
    }

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
