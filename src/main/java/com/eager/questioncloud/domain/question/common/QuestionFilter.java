package com.eager.questioncloud.domain.question.common;

import com.eager.questioncloud.domain.question.vo.QuestionLevel;
import com.eager.questioncloud.domain.question.vo.QuestionType;
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
