package com.eager.questioncloud.question.dto;

import com.eager.questioncloud.question.domain.QuestionLevel;
import com.eager.questioncloud.question.domain.QuestionSortType;
import com.eager.questioncloud.question.domain.QuestionType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@AllArgsConstructor
@Getter
public class QuestionFilter {
    private Long userId;
    private List<Long> categories;
    private List<QuestionLevel> levels;
    private QuestionType questionType;
    private Long creatorId;
    private QuestionSortType sort;
    private Pageable pageable;
}
