package com.eager.questioncloud.domain.question.dto;

import com.eager.questioncloud.domain.question.vo.Subject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestionCategoryGroupBySubject {
    private Subject subject;
    private List<MainQuestionCategory> list;

    public static List<QuestionCategoryGroupBySubject> create(List<MainQuestionCategory> mainQuestionCategoryList) {
        List<QuestionCategoryGroupBySubject> result = new ArrayList<>();
        mainQuestionCategoryList
            .stream()
            .collect(Collectors.groupingBy(mainQuestionCategory -> mainQuestionCategory.subject))
            .forEach((subject, items) -> result.add(new QuestionCategoryGroupBySubject(subject, items)));

        result.sort(Comparator.comparing(data -> data.getSubject().getValue()));

        return result;
    }

    @Getter
    @AllArgsConstructor
    public static class MainQuestionCategory {
        private String title;
        private Subject subject;
        private List<SubQuestionCategory> sub;
    }

    @Getter
    @AllArgsConstructor
    public static class SubQuestionCategory {
        private Long id;
        private String title;
    }
}
