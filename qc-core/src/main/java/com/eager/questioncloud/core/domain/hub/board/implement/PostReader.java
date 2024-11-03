package com.eager.questioncloud.core.domain.hub.board.implement;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.hub.board.dto.PostDto.PostDetail;
import com.eager.questioncloud.core.domain.hub.board.dto.PostDto.PostListItem;
import com.eager.questioncloud.core.domain.hub.board.model.Post;
import com.eager.questioncloud.core.domain.hub.board.repository.QuestionBoardRepository;
import com.eager.questioncloud.core.domain.hub.question.implement.QuestionPermissionValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostReader {
    private final QuestionBoardRepository questionBoardRepository;
    private final QuestionPermissionValidator questionPermissionValidator;

    public Post findById(Long boardId) {
        return questionBoardRepository.findById(boardId);
    }

    public PostDetail getQuestionBoardDetail(Long userId, Long boardId) {
        PostDetail questionBoard = questionBoardRepository.getQuestionBoardDetail(boardId);
        questionPermissionValidator.permissionValidator(userId, questionBoard.getQuestionId());
        return questionBoard;
    }

    public List<PostListItem> getQuestionBoardList(Long userId, Long questionId, PagingInformation pagingInformation) {
        questionPermissionValidator.permissionValidator(userId, questionId);
        return questionBoardRepository.getQuestionBoardList(questionId, pagingInformation);
    }

    public int count(Long questionId) {
        return questionBoardRepository.count(questionId);
    }

    public List<PostListItem> getCreatorQuestionBoardList(Long creatorId, PagingInformation pagingInformation) {
        return questionBoardRepository.getCreatorQuestionBoardList(creatorId, pagingInformation);
    }

    public int countCreatorQuestionBoard(Long creatorId) {
        return questionBoardRepository.countCreatorQuestionBoard(creatorId);
    }
}
