package com.eager.questioncloud.core.domain.hub.question.dto;

import com.eager.questioncloud.core.domain.hub.question.vo.Subject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class QuestionCategoryDto {
    @Getter
    @AllArgsConstructor
    public static class QuestionCategoryListItem {
        private Subject subject;
        private List<QuestionCategoryItem> list;

        public static List<QuestionCategoryListItem> groupBy(List<QuestionCategoryItem> questionCategoryItemList) {
            List<QuestionCategoryListItem> result = new ArrayList<>();
            questionCategoryItemList
                .stream()
                .collect(Collectors.groupingBy(questionCategoryItem -> questionCategoryItem.subject))
                .forEach((subject, items) -> result.add(new QuestionCategoryListItem(subject, items)));
            return result;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class QuestionCategoryItem {
        private String title;
        private Subject subject;
        private List<SubQuestionCategoryItem> sub;
    }

    @Getter
    @AllArgsConstructor
    public static class SubQuestionCategoryItem {
        private Long id;
        private String title;
    }
}
