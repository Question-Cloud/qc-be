package com.eager.questioncloud.creator.domain;

import com.eager.questioncloud.creator.entity.CreatorEntity;
import com.eager.questioncloud.question.domain.Subject;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Creator {
    private Long id;
    private Long userId;
    private Subject mainSubject;
    private String introduction;

    @Builder
    public Creator(Long id, Long userId, Subject mainSubject, String introduction) {
        this.id = id;
        this.userId = userId;
        this.mainSubject = mainSubject;
        this.introduction = introduction;
    }

    public static Creator create(Long userId, Subject mainSubject, String introduction) {
        return Creator.builder()
            .userId(userId)
            .mainSubject(mainSubject)
            .introduction(introduction)
            .build();
    }

    public void update(Subject mainSubject, String introduction) {
        this.mainSubject = mainSubject;
        this.introduction = introduction;
    }

    public CreatorEntity toEntity() {
        return CreatorEntity.builder()
            .id(id)
            .userId(userId)
            .mainSubject(mainSubject)
            .introduction(introduction)
            .build();
    }
}
