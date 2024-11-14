package com.eager.questioncloud.core.domain.workspace.implement;

import com.eager.questioncloud.core.domain.question.model.ModifyQuestion;
import com.eager.questioncloud.core.domain.question.model.Question;
import com.eager.questioncloud.core.domain.question.model.QuestionCategory;
import com.eager.questioncloud.core.domain.question.model.QuestionCategoryInformation;
import com.eager.questioncloud.core.domain.question.repository.QuestionCategoryRepository;
import com.eager.questioncloud.core.domain.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatorQuestionUpdater {
    private final QuestionRepository questionRepository;
    private final QuestionCategoryRepository questionCategoryRepository;

    public void modifyQuestionContent(ModifyQuestion modifyQuestion) {
        Question question = questionRepository.findByQuestionIdAndCreatorId(modifyQuestion.getQuestionId(), modifyQuestion.getCreator().getId());

        QuestionCategory childCategory = questionCategoryRepository.findById(modifyQuestion.getQuestionCategoryId());
        QuestionCategory parentCategory = questionCategoryRepository.findById(childCategory.getParentId());
        QuestionCategoryInformation questionCategoryInformation = new QuestionCategoryInformation(parentCategory, childCategory);

        question.modify(modifyQuestion.getQuestionContent(), questionCategoryInformation);
        
        questionRepository.save(question);
    }
}
