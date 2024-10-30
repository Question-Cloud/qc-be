package com.eager.questioncloud.core.domain.hub.question.common;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.hub.question.vo.QuestionLevel;
import com.eager.questioncloud.core.domain.hub.question.vo.QuestionType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class QuestionFilter {
    private Long userId;
    private List<Long> categories;
    private List<QuestionLevel> levels;
    private QuestionType questionType;
    private Long creatorId;
    private QuestionSortType sort;
    private PagingInformation pagingInformation;
}
