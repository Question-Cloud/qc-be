package com.eager.questioncloud.domain.post;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Post {
    private Long id;
    private Long questionId;
    private Long writerId;
    private PostContent postContent;
    private LocalDateTime createdAt;

    @Builder
    public Post(Long id, Long questionId, Long writerId, PostContent postContent, LocalDateTime createdAt) {
        this.id = id;
        this.questionId = questionId;
        this.writerId = writerId;
        this.postContent = postContent;
        this.createdAt = createdAt;
    }

    public static Post create(Long questionId, Long writerId, PostContent postContent) {
        return Post.builder()
            .questionId(questionId)
            .writerId(writerId)
            .postContent(postContent)
            .createdAt(LocalDateTime.now())
            .build();
    }

    public void updateQuestionBoardContent(PostContent postContent) {
        this.postContent = postContent;
    }
}
