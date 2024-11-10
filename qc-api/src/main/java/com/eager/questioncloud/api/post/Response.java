package com.eager.questioncloud.api.post;

import com.eager.questioncloud.core.domain.hub.post.dto.PostDto.PostDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class Response {
    @Getter
    @AllArgsConstructor
    public static class PostResponse {
        private PostDetail board;
    }
}
