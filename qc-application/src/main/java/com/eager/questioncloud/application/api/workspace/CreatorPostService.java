package com.eager.questioncloud.application.api.workspace;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.post.PostListItem;
import com.eager.questioncloud.core.domain.post.PostRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatorPostService {
    private final PostRepository postRepository;

    public List<PostListItem> getCreatorPosts(Long creatorId, PagingInformation pagingInformation) {
        return postRepository.getCreatorPostList(creatorId, pagingInformation);
    }

    public int countCreatorPost(Long creatorId) {
        return postRepository.countCreatorPost(creatorId);
    }
}
