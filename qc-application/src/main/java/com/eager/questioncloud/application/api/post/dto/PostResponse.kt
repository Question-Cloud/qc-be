package com.eager.questioncloud.application.api.post.dto;

import com.eager.questioncloud.core.domain.post.dto.PostDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class PostControllerResponse {
    @Getter
    @AllArgsConstructor
    public static class PostResponse {
        private PostDetail board;
    }
}
