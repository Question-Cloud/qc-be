package com.eager.questioncloud.core.domain.question.dto;

import com.eager.questioncloud.core.domain.question.model.Question;
import com.eager.questioncloud.core.domain.question.vo.QuestionLevel;
import com.eager.questioncloud.core.domain.question.vo.Subject;
import com.eager.questioncloud.core.domain.review.model.QuestionReviewStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class QuestionDto {
    @Getter
    @AllArgsConstructor
    @Builder
    public static class QuestionInformation {
        private Long id;
        private String title;
        private String parentCategory;
        private String childCategory;
        private String thumbnail;
        private String creatorName;
        private QuestionLevel questionLevel;
        private int price;
        private Double rate;
        private Boolean isOwned;

        public static List<QuestionInformation> of(List<Question> questions, Set<Long> alreadyOwnedQuestionIds,
            Map<Long, QuestionReviewStatistics> reviewStatistics) {
            return questions.stream()
                .map(question -> QuestionInformation.builder()
                    .id(question.getId())
                    .title(question.getQuestionContent().getTitle())
                    .parentCategory(question.getCategory().getParentCategory().getTitle())
                    .childCategory(question.getCategory().getChildCategory().getTitle())
                    .thumbnail(question.getQuestionContent().getThumbnail())
                    .creatorName(question.getCreator().getUser().getUserInformation().getName())
                    .questionLevel(question.getQuestionContent().getQuestionLevel())
                    .price(question.getQuestionContent().getPrice())
                    .rate(reviewStatistics.get(question.getId()).getAverageRate())
                    .isOwned(alreadyOwnedQuestionIds.contains(question.getId()))
                    .build())
                .collect(Collectors.toList());
        }

        public static QuestionInformation forHubDetail(Question question, Boolean isOwned, QuestionReviewStatistics questionReviewStatistics) {
            return QuestionInformation.builder()
                .id(question.getId())
                .title(question.getQuestionContent().getTitle())
                .parentCategory(question.getCategory().getParentCategory().getTitle())
                .childCategory(question.getCategory().getChildCategory().getTitle())
                .thumbnail(question.getQuestionContent().getThumbnail())
                .creatorName(question.getCreator().getUser().getUserInformation().getName())
                .questionLevel(question.getQuestionContent().getQuestionLevel())
                .price(question.getQuestionContent().getPrice())
                .rate(questionReviewStatistics.getAverageRate())
                .isOwned(isOwned)
                .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class QuestionInformationForLibrary {
        private Long id;
        private String title;
        private Subject subject;
        private String parentCategory;
        private String childCategory;
        private String thumbnail;
        private String creatorName;
        private QuestionLevel questionLevel;
        private String fileUrl;
        private String explanationUrl;

        public static QuestionInformationForLibrary forLibraryDetail(Question question) {
            return QuestionInformationForLibrary.builder()
                .id(question.getId())
                .title(question.getQuestionContent().getTitle())
                .subject(question.getCategory().getChildCategory().getSubject())
                .parentCategory(question.getCategory().getParentCategory().getTitle())
                .childCategory(question.getCategory().getChildCategory().getTitle())
                .thumbnail(question.getQuestionContent().getThumbnail())
                .creatorName(question.getCreator().getUser().getUserInformation().getName())
                .questionLevel(question.getQuestionContent().getQuestionLevel())
                .fileUrl(question.getQuestionContent().getFileUrl())
                .explanationUrl(question.getQuestionContent().getExplanationUrl())
                .build();
        }
    }
}
