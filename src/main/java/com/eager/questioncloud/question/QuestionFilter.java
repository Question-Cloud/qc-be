package com.eager.questioncloud.question;

import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@AllArgsConstructor
@Getter
public class QuestionFilter {
    @Parameter(hidden = true)
    private Long userId;
    private List<Long> categories;
    private List<QuestionLevel> levels;
    private QuestionType questionType;
    private Long creatorId;
    private QuestionSortType sort;
    private Pageable pageable;
}
