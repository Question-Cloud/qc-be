package com.eager.questioncloud.core.domain.workspace.service;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.post.dto.PostDto.PostListItem;
import com.eager.questioncloud.core.domain.workspace.implement.CreatorPostReader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatorPostService {
    private final CreatorPostReader creatorPostReader;

    public List<PostListItem> getCreatorPosts(Long creatorId, PagingInformation pagingInformation) {
        return creatorPostReader.getCreatorPosts(creatorId, pagingInformation);
    }

    public int countCreatorPost(Long creatorId) {
        return creatorPostReader.countCreatorPost(creatorId);
    }
}
