package com.eager.questioncloud.application.api.post;

import com.eager.questioncloud.core.domain.post.infrastructure.PostRepository;
import com.eager.questioncloud.core.domain.post.model.Post;
import com.eager.questioncloud.core.domain.question.infrastructure.QuestionRepository;
import com.eager.questioncloud.core.domain.userquestion.UserQuestionRepository;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostPermissionChecker {
    private final QuestionRepository questionRepository;
    private final UserQuestionRepository userQuestionRepository;
    private final PostRepository postRepository;

    public Boolean hasPermission(Long userId, Long questionId) {
        if (!questionRepository.isAvailable(questionId)) {
            throw new CustomException(Error.UNAVAILABLE_QUESTION);
        }

        if (!userQuestionRepository.isOwned(userId, questionId)) {
            throw new CustomException(Error.NOT_OWNED_QUESTION);
        }

        return true;
    }

    public Boolean hasCommentPermission(Long userId, Long postId) {
        Post post = postRepository.findById(postId);
        return hasPermission(userId, post.getQuestionId());
    }
}
