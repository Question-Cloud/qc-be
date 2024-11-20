package com.eager.questioncloud.application.api.post;

import com.eager.questioncloud.domain.post.PostDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class PostControllerResponse {
    @Getter
    @AllArgsConstructor
    public static class PostResponse {
        private PostDetail board;
    }
}
