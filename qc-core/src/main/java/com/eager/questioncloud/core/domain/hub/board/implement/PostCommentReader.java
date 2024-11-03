package com.eager.questioncloud.core.domain.hub.board.implement;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.hub.board.dto.PostCommentDto.PostCommentDetail;
import com.eager.questioncloud.core.domain.hub.board.repository.PostCommentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostCommentReader {
    private final PostCommentRepository postCommentRepository;
    private final PostValidator postValidator;

    public List<PostCommentDetail> getPostComments(Long postId, Long userId, PagingInformation pagingInformation) {
        postValidator.permissionValidator(postId, userId);
        return postCommentRepository.getPostCommentDetails(postId, userId, pagingInformation);
    }

    public int count(Long boardId) {
        return postCommentRepository.count(boardId);
    }
}
