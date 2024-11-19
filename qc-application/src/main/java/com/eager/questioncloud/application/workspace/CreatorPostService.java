package com.eager.questioncloud.application.workspace;

import com.eager.questioncloud.common.PagingInformation;
import com.eager.questioncloud.domain.post.PostListItem;
import com.eager.questioncloud.domain.post.PostRepository;
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
