package com.eager.questioncloud.core.domain.hub.post.implement;

import com.eager.questioncloud.core.domain.hub.post.model.Post;
import com.eager.questioncloud.core.domain.hub.question.implement.QuestionPermissionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostValidator {
    private final PostReader postReader;
    private final QuestionPermissionValidator questionPermissionValidator;

    public void permissionValidator(Long postId, Long userId) {
        Post post = postReader.findById(postId);
        questionPermissionValidator.permissionValidator(userId, post.getQuestionId());
    }
}
