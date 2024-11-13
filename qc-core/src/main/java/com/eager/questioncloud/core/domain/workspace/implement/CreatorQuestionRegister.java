package com.eager.questioncloud.core.domain.workspace.implement;

import com.eager.questioncloud.core.domain.question.model.Question;
import com.eager.questioncloud.core.domain.question.model.QuestionCategory;
import com.eager.questioncloud.core.domain.question.model.QuestionCategoryInformation;
import com.eager.questioncloud.core.domain.question.repository.QuestionCategoryRepository;
import com.eager.questioncloud.core.domain.question.repository.QuestionRepository;
import com.eager.questioncloud.core.domain.workspace.model.RegisterQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreatorQuestionRegister {
    private final QuestionRepository questionRepository;
    private final QuestionCategoryRepository questionCategoryRepository;

    @Transactional
    public Question register(RegisterQuestion registerQuestion) {
        QuestionCategory childCategory = questionCategoryRepository.findById(registerQuestion.getQuestionCategoryId());
        QuestionCategory parentCategory = questionCategoryRepository.findById(childCategory.getParentId());
        QuestionCategoryInformation questionCategoryInformation = new QuestionCategoryInformation(parentCategory, childCategory);

        Question question = Question.create(registerQuestion.getCreator(), registerQuestion.getQuestionContent(), questionCategoryInformation);

        return questionRepository.save(question);
    }
}
