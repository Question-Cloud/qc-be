package com.eager.questioncloud.question;

import com.eager.questioncloud.common.PagingResponse;
import com.eager.questioncloud.question.QuestionDto.QuestionInformation;
import com.eager.questioncloud.question.Response.QuestionInformationResponse;
import com.eager.questioncloud.security.UserPrincipal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/question")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping
    public PagingResponse<QuestionInformation> getQuestionListByFiltering(QuestionFilter questionFilter) {
        int total = questionService.getTotalFiltering(questionFilter);
        List<QuestionInformation> questionInformation = questionService.getQuestionListByFiltering(questionFilter);
        return new PagingResponse<>(total, questionInformation);
    }

    @GetMapping("/{questionId}")
    public QuestionInformationResponse getQuestionDetail(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long questionId) {
        QuestionInformation questionInformation = questionService.getQuestionInformation(questionId, userPrincipal.getUser().getUid());
        return new QuestionInformationResponse(questionInformation);
    }
}
