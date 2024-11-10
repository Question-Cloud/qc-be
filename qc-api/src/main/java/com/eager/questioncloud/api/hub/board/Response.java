package com.eager.questioncloud.api.hub.board;

import com.eager.questioncloud.core.domain.hub.board.dto.PostDto.PostDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class Response {
    @Getter
    @AllArgsConstructor
    public static class PostResponse {
        private PostDetail board;
    }
}
