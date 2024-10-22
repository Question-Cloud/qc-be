package com.eager.questioncloud.domain.creator.implement;

import com.eager.questioncloud.domain.creator.model.Creator;
import com.eager.questioncloud.domain.creator.repository.CreatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatorAppender {
    private final CreatorRepository creatorRepository;

    public Creator append(Creator creator) {
        return creatorRepository.save(creator);
    }
}
