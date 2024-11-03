package com.eager.questioncloud.core.domain.hub.board.implement;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.hub.board.dto.PostDto.PostDetail;
import com.eager.questioncloud.core.domain.hub.board.dto.PostDto.PostListItem;
import com.eager.questioncloud.core.domain.hub.board.model.Post;
import com.eager.questioncloud.core.domain.hub.board.repository.PostRepository;
import com.eager.questioncloud.core.domain.hub.question.implement.QuestionPermissionValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostReader {
    private final PostRepository postRepository;
    private final QuestionPermissionValidator questionPermissionValidator;

    public Post findById(Long postId) {
        return postRepository.findById(postId);
    }

    public PostDetail getPostDetail(Long userId, Long postId) {
        PostDetail questionBoard = postRepository.getPostDetail(postId);
        questionPermissionValidator.permissionValidator(userId, questionBoard.getQuestionId());
        return questionBoard;
    }

    public List<PostListItem> getPosts(Long userId, Long questionId, PagingInformation pagingInformation) {
        questionPermissionValidator.permissionValidator(userId, questionId);
        return postRepository.getPostList(questionId, pagingInformation);
    }

    public int count(Long questionId) {
        return postRepository.count(questionId);
    }

    public List<PostListItem> getCreatorPostList(Long creatorId, PagingInformation pagingInformation) {
        return postRepository.getCreatorPostList(creatorId, pagingInformation);
    }

    public int countCreatorPost(Long creatorId) {
        return postRepository.countCreatorPost(creatorId);
    }
}
