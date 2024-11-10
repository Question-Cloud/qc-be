package com.eager.questioncloud.core.domain.workspace.implement;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.post.dto.PostDto.PostListItem;
import com.eager.questioncloud.core.domain.post.repository.PostRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatorPostReader {
    private final PostRepository postRepository;

    public List<PostListItem> getCreatorPosts(Long creatorId, PagingInformation pagingInformation) {
        return postRepository.getCreatorPostList(creatorId, pagingInformation);
    }

    public int countCreatorPost(Long creatorId) {
        return postRepository.countCreatorPost(creatorId);
    }
}
