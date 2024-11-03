package com.eager.questioncloud.core.domain.hub.board.service;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.hub.board.dto.PostDto.PostDetail;
import com.eager.questioncloud.core.domain.hub.board.dto.PostDto.PostListItem;
import com.eager.questioncloud.core.domain.hub.board.implement.PostAppender;
import com.eager.questioncloud.core.domain.hub.board.implement.PostReader;
import com.eager.questioncloud.core.domain.hub.board.implement.PostRemover;
import com.eager.questioncloud.core.domain.hub.board.implement.PostUpdater;
import com.eager.questioncloud.core.domain.hub.board.model.Post;
import com.eager.questioncloud.core.domain.hub.board.vo.PostContent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionBoardService {
    private final PostAppender postAppender;
    private final PostReader postReader;
    private final PostUpdater postUpdater;
    private final PostRemover postRemover;

    public Post register(Post post) {
        return postAppender.append(post);
    }

    public List<PostListItem> getQuestionBoardList(Long userId, Long questionId, PagingInformation pagingInformation) {
        return postReader.getPosts(userId, questionId, pagingInformation);
    }

    public int countQuestionBoard(Long questionId) {
        return postReader.count(questionId);
    }

    public PostDetail getQuestionBoardDetail(Long userId, Long boardId) {
        return postReader.getPostDetail(userId, boardId);
    }

    public void modify(Long boardId, Long userId, PostContent postContent) {
        postUpdater.updatePost(boardId, userId, postContent);
    }

    public void delete(Long boardId, Long userId) {
        postRemover.delete(boardId, userId);
    }
}
