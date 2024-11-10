package com.eager.questioncloud.core.domain.post.implement;

import com.eager.questioncloud.core.domain.post.model.Post;
import com.eager.questioncloud.core.domain.post.repository.PostRepository;
import com.eager.questioncloud.core.domain.question.implement.QuestionPermissionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostAppender {
    private final PostRepository postRepository;
    private final QuestionPermissionValidator questionPermissionValidator;

    public Post append(Post post) {
        questionPermissionValidator.permissionValidator(post.getWriterId(), post.getQuestionId());
        return postRepository.save(post);
    }
}
