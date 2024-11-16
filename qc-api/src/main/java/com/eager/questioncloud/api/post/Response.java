package com.eager.questioncloud.api.post;

import com.eager.questioncloud.core.domain.post.dto.PostDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class Response {
    @Getter
    @AllArgsConstructor
    public static class PostResponse {
        private PostDetail board;
    }
}
