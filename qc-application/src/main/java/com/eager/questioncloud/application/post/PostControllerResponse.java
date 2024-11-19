package com.eager.questioncloud.application.post;

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
