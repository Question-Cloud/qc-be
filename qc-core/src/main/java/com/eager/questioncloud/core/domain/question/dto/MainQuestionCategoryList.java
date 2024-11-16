package com.eager.questioncloud.core.domain.question.dto;

import com.eager.questioncloud.core.domain.question.vo.Subject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MainQuestionCategoryList {
    private Subject subject;
    private List<MainQuestionCategory> list;

    public static List<MainQuestionCategoryList> create(List<MainQuestionCategory> mainQuestionCategoryList) {
        List<MainQuestionCategoryList> result = new ArrayList<>();
        mainQuestionCategoryList
            .stream()
            .collect(Collectors.groupingBy(mainQuestionCategory -> mainQuestionCategory.subject))
            .forEach((subject, items) -> result.add(new MainQuestionCategoryList(subject, items)));
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
