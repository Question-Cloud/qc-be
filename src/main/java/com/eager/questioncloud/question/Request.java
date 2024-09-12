package com.eager.questioncloud.question;

import lombok.Getter;

public class Request {
    @Getter
    public static class RegisterSelfMadeQuestionRequest {
        private Long questionCategoryId;
        private Subject subject;
        private String title;
        private String description;
        private String thumbnail;
        private String fileUrl;
        private String explanationUrl;
        private QuestionLevel questionLevel;
        private int price;

        public QuestionContent toModel() {
            return new QuestionContent(
                questionCategoryId,
                subject, title,
                description,
                thumbnail,
                fileUrl,
                explanationUrl,
                QuestionType.SelfMade,
                questionLevel,
                QuestionStatus.Available,
                price);
        }
    }

    @Getter
    public static class ModifySelfMadeQuestionRequest {
        private Long questionCategoryId;
        private Subject subject;
        private String title;
        private String description;
        private String thumbnail;
        private String fileUrl;
        private String explanationUrl;
        private QuestionLevel questionLevel;
        private QuestionStatus questionStatus;
        private int price;

        public QuestionContent toModel() {
            return new QuestionContent(
                questionCategoryId,
                subject, title,
                description,
                thumbnail,
                fileUrl,
                explanationUrl,
                QuestionType.SelfMade,
                questionLevel,
                questionStatus,
                price);
        }
    }
}
