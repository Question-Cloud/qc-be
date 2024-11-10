package com.eager.questioncloud.core.domain.workspace.service;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.creator.implement.CreatorUpdater;
import com.eager.questioncloud.core.domain.creator.model.Creator;
import com.eager.questioncloud.core.domain.creator.vo.CreatorProfile;
import com.eager.questioncloud.core.domain.post.dto.PostDto.PostListItem;
import com.eager.questioncloud.core.domain.workspace.implement.CreatorPostReader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatorWorkSpaceService {
    private final CreatorPostReader countCreatorPost;
    private final CreatorUpdater creatorUpdater;


    public List<PostListItem> getCreatorQuestionBoardList(Long creatorId, PagingInformation pagingInformation) {
        return countCreatorPost.getCreatorPosts(creatorId, pagingInformation);
    }

    public int countCreatorQuestionBoardList(Long creatorId) {
        return countCreatorPost.countCreatorPost(creatorId);
    }

    public void updateCreatorProfile(Creator creator, CreatorProfile creatorProfile) {
        creatorUpdater.updateCreatorProfile(creator, creatorProfile);
    }
}
