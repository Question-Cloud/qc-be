package com.eager.questioncloud.board;

import com.eager.questioncloud.board.QuestionBoardDto.QuestionBoardListItem;
import com.eager.questioncloud.creator.Creator;
import com.eager.questioncloud.creator.CreatorReader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatorQuestionBoardReader {
    private final QuestionBoardRepository questionBoardRepository;
    private final CreatorReader creatorReader;

    public List<QuestionBoardListItem> getCreatorQuestionBoardList(Long userId, Pageable pageable) {
        Creator creator = creatorReader.getByUserId(userId);
        return questionBoardRepository.getCreatorQuestionBoardList(creator.getId(), pageable);
    }

    public int countCreatorQuestionBoard(Long userId) {
        Creator creator = creatorReader.getByUserId(userId);
        return questionBoardRepository.countCreatorQuestionBoard(creator.getId());
    }
}
